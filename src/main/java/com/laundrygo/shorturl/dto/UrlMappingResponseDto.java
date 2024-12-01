package com.laundrygo.shorturl.dto;

import com.laundrygo.shorturl.domain.UrlMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UrlMappingResponseDto {
    private String originalUrl;
    private String shortUrl;
    private Long requestCount;


    public static UrlMappingResponseDto from(UrlMapping entity) {
        UrlMappingResponseDto dto = new UrlMappingResponseDto();
        dto.setOriginalUrl(entity.getOriginalUrl());
        dto.setShortUrl(entity.getShortUrl());
        dto.setRequestCount(entity.getRequestCount());
        return dto;
    }
}
