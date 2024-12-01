package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.dto.UrlMappingResponseDto;
import com.laundrygo.shorturl.service.UrlShorteningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlShorteningService service;

    public UrlController(UrlShorteningService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        String shortUrl = service.shortenUrl(originalUrl);
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = service.getOriginalUrl(shortUrl);
        return ResponseEntity.ok(originalUrl);
    }

    @GetMapping
    public ResponseEntity<List<UrlMapping>> getAllMappings() {
        List<UrlMapping> mappings = service.getAllMappings();
        return ResponseEntity.ok(mappings);
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<UrlMappingResponseDto>> getUrlStatistics() {
        List<UrlMappingResponseDto> statistics = service.getAllMappings().stream()
                .map(UrlMappingResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statistics);
    }
}
