package com.learning.urlshortener.controller;

import com.learning.urlshortener.UrlShortenerApplication;
import com.learning.urlshortener.dto.LinkStatsResponse;
import com.learning.urlshortener.dto.ShortenRequest;
import com.learning.urlshortener.dto.ShortenResponse;
import com.learning.urlshortener.entity.UrlMapping;
import com.learning.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class UrlShortenController {
    private final UrlService urlService;
    public UrlShortenController(UrlService urlService) {
        this.urlService = urlService;
    }
    @PostMapping("/shorten")
    public ShortenResponse shorten(@RequestBody ShortenRequest shortenRequest) {
        String shortCode= urlService.getShortCode(shortenRequest.getOriginalUrl());
        return new ShortenResponse(shortCode);
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getYourOriginalUrl(shortCode);
        response.sendRedirect(originalUrl);
    }

    @GetMapping("/{shortCode}/stats")
    public LinkStatsResponse getStats(@PathVariable String shortCode) {
        UrlMapping mapping = urlService.getMapping(shortCode);
        return new LinkStatsResponse(mapping.getOriginalUrl(), mapping.getClickCount());
    }
}
