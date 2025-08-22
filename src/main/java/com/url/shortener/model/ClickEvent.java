package com.url.shortener.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "click_event")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime clickDate;

    @ManyToOne
    @JoinColumn(name = "url_mapping_id")
    UrlMapping urlMapping;

}
