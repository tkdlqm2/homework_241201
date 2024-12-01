package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.exception.url.UrlErrorCode;
import com.laundrygo.shorturl.exception.url.UrlException;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShorteningServiceTest {
    private static final String ORIGINAL_URL = "https://www.example.com";
    private static final String SHORT_URL = "abcd1234";

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private UrlShorteningService service;

    @Test
    void getOriginalUrl_WhenShortUrlExists_ShouldIncrementRequestCount() {
        // Given
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(ORIGINAL_URL);
        mapping.setShortUrl(SHORT_URL);
        mapping.setRequestCount(0L);

        when(repository.findByShortUrl(SHORT_URL)).thenReturn(Optional.of(mapping));
        when(repository.save(any(UrlMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        String result = service.getOriginalUrl(SHORT_URL);

        // Then
        assertThat(result).isEqualTo(ORIGINAL_URL);
        assertThat(mapping.getRequestCount()).isEqualTo(1L);
        verify(repository).findByShortUrl(SHORT_URL);
        verify(repository).save(any(UrlMapping.class));
    }

    @Test
    void getOriginalUrl_WhenShortUrlNotFound_ShouldThrowException() {
        // Given
        when(repository.findByShortUrl(SHORT_URL)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> service.getOriginalUrl(SHORT_URL))
                .isInstanceOf(UrlException.class)
                .satisfies(exception -> {
                    UrlException urlException = (UrlException) exception;
                    assertThat(urlException.getCommonErrorCodeType()).isEqualTo(UrlErrorCode.URL_NOT_FOUND);
                    assertThat(urlException.getMessage()).isEqualTo(UrlErrorCode.URL_NOT_FOUND.getMessage());
                });
    }

    @Test
    void shortenUrl_WhenInvalidUrl_ShouldThrowException() {
        // Given
        String invalidUrl = "invalid-url";

        // When & Then
        assertThatThrownBy(() -> service.shortenUrl(invalidUrl))
                .isInstanceOf(UrlException.class)
                .satisfies(exception -> {
                    UrlException urlException = (UrlException) exception;
                    assertThat(urlException.getCommonErrorCodeType()).isEqualTo(UrlErrorCode.INVALID_URL_FORMAT);
                    assertThat(urlException.getMessage()).isEqualTo(UrlErrorCode.INVALID_URL_FORMAT.getMessage());
                });
    }

    @Test
    void shortenUrl_WhenDuplicateShortUrl_ShouldThrowException() {
        // Given
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(ORIGINAL_URL);
        mapping.setShortUrl(SHORT_URL);

        when(repository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.empty());
        when(repository.save(any(UrlMapping.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // When & Then
        assertThatThrownBy(() -> service.shortenUrl(ORIGINAL_URL))
                .isInstanceOf(UrlException.class)
                .satisfies(exception -> {
                    UrlException urlException = (UrlException) exception;
                    assertThat(urlException.getCommonErrorCodeType()).isEqualTo(UrlErrorCode.DUPLICATE_SHORT_URL);
                    assertThat(urlException.getMessage()).isEqualTo(UrlErrorCode.DUPLICATE_SHORT_URL.getMessage());
                });
    }


    @Test
    void shortenUrl_WhenUrlAlreadyExists_ShouldReturnExistingShortUrl() {
        // Given
        UrlMapping existingMapping = new UrlMapping();
        existingMapping.setOriginalUrl(ORIGINAL_URL);
        existingMapping.setShortUrl(SHORT_URL);

        when(repository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.of(existingMapping));

        // When
        String result = service.shortenUrl(ORIGINAL_URL);

        // Then
        assertThat(result).isEqualTo(SHORT_URL);
        verify(repository).findByOriginalUrl(ORIGINAL_URL);
        verify(repository, never()).save(any());
    }
}
