package com.url.shortener.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UrlMappingDto {
    private Long id;
    private String shortUrl;
    private String originalUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private String username;
}
