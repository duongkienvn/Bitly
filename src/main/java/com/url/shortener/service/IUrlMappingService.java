package com.url.shortener.service;

import com.url.shortener.dto.ClickEventDto;
import com.url.shortener.dto.UrlMappingDto;
import com.url.shortener.model.UrlMapping;
import com.url.shortener.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IUrlMappingService {
    UrlMappingDto createShortUrl(String originalUrl, User user);
    List<UrlMappingDto> getMyUrls(User user);
    List<ClickEventDto> getClickEventsByDate(String shortUrl, LocalDateTime startDate, LocalDateTime endDate);
    Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate startDate, LocalDate endDate);
    UrlMapping getOriginalUrlByShortUrl(String shortUrl);
}
