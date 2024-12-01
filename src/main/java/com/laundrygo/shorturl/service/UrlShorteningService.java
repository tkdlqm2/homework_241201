package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.exception.url.UrlErrorCode;
import com.laundrygo.shorturl.exception.url.UrlException;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlShorteningService {
    private final UrlMappingRepository repository;
    private static final int MAX_ATTEMPTS = 5;
    private static final int SHORT_URL_LENGTH = 8;

    @Transactional
    public String shortenUrl(String originalUrl) {
        validateUrl(originalUrl);

        return repository.findByOriginalUrl(originalUrl)
                .map(UrlMapping::getShortUrl)
                .orElseGet(() -> generateUniqueShortUrl(originalUrl));
    }

    private void validateUrl(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new UrlException(UrlErrorCode.INVALID_URL_FORMAT);
        }
    }

    private String generateUniqueShortUrl(String originalUrl) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                String shortUrl = createShortUrl(originalUrl, attempts);
                saveUrlMapping(originalUrl, shortUrl);
                return shortUrl;
            } catch (DataIntegrityViolationException e) {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    throw new UrlException(UrlErrorCode.URL_GENERATION_FAILED);
                }
            }
        }
        throw new UrlException(UrlErrorCode.URL_GENERATION_FAILED);
    }

    private String createShortUrl(String originalUrl, int attempt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            String input = originalUrl + attempt;
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            return encoded.substring(0, SHORT_URL_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            throw new UrlException(UrlErrorCode.URL_GENERATION_FAILED);
        }
    }

    @Transactional
    private void saveUrlMapping(String originalUrl, String shortUrl) {
        try {
            UrlMapping mapping = new UrlMapping();
            mapping.setOriginalUrl(originalUrl);
            mapping.setShortUrl(shortUrl);
            mapping.setRequestCount(0L);
            repository.save(mapping);
        } catch (DataIntegrityViolationException e) {
            throw new UrlException(UrlErrorCode.DUPLICATE_SHORT_URL);
        }
    }

    @Transactional
    public String getOriginalUrl(String shortUrl) {
        UrlMapping mapping = repository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlException(UrlErrorCode.URL_NOT_FOUND));

        mapping.setRequestCount(mapping.getRequestCount() + 1);
        repository.save(mapping);

        return mapping.getOriginalUrl();
    }

    public List<UrlMapping> getAllMappings() {
        return repository.findAll();
    }
}

