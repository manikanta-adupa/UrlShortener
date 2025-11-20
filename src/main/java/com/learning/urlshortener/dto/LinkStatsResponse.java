package com.learning.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkStatsResponse {
    private String originalUrl;
    private long clickCount;
}
