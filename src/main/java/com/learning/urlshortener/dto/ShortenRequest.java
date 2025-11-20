package com.learning.urlshortener.dto;

import lombok.Data;

@Data
public class ShortenRequest {
    private String originalUrl;
}
