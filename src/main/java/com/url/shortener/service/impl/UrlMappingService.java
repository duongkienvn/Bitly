package com.url.shortener.service.impl;

import com.url.shortener.dto.ClickEventDto;
import com.url.shortener.dto.UrlMappingDto;
import com.url.shortener.model.ClickEvent;
import com.url.shortener.model.UrlMapping;
import com.url.shortener.model.User;
import com.url.shortener.repository.ClickEventRepository;
import com.url.shortener.repository.UrlMappingRepository;
import com.url.shortener.service.IUrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlMappingService implements IUrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final Random random = new Random();
    private final ClickEventRepository clickEventRepository;

    @Override
    public UrlMappingDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = UrlMapping.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .user(user)
                .createdDate(LocalDateTime.now())
                .build();

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertUrlMappingToDto(savedUrlMapping);
    }

    private UrlMappingDto convertUrlMappingToDto(UrlMapping urlMapping) {
        return UrlMappingDto.builder()
                .id(urlMapping.getId())
                .shortUrl(urlMapping.getShortUrl())
                .originalUrl(urlMapping.getOriginalUrl())
                .clickCount(urlMapping.getClickcount())
                .createdDate(urlMapping.getCreatedDate())
                .username(urlMapping.getUser().getUsername())
                .build();
    }

    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }

    @Override
    public List<UrlMappingDto> getMyUrls(User user) {
        return urlMappingRepository.findByUser_Id(user.getId())
                .stream()
                .map(this::convertUrlMappingToDto)
                .toList();
    }

    @Override
    public List<ClickEventDto> getClickEventsByDate(String shortUrl, LocalDateTime startDate, LocalDateTime endDate) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, startDate, endDate)
                    .stream()
                    .collect(Collectors.groupingBy(clickEvent ->
                                    clickEvent.getClickDate().toLocalDate(),
                            Collectors.counting())).entrySet().stream().map(entry -> {
                        ClickEventDto clickEventDto = new ClickEventDto();
                        clickEventDto.setClickDate(entry.getKey());
                        clickEventDto.setCount(entry.getValue());
                        return clickEventDto;
                    }).collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate startDate, LocalDate endDate) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser_Id(user.getId());
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings,
                startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        return clickEvents.stream()
                .collect(Collectors.groupingBy(clickEvent -> clickEvent.getClickDate().toLocalDate(),
                Collectors.counting()));
    }

    @Override
    public UrlMapping getOriginalUrlByShortUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            urlMapping.setClickcount(urlMapping.getClickcount() + 1);
            urlMappingRepository.save(urlMapping);

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);

            return urlMapping;
        }

        return null;
    }
}
