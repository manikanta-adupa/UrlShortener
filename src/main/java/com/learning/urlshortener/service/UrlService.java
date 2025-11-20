package com.learning.urlshortener.service;

import com.learning.urlshortener.entity.UrlMapping;
import com.learning.urlshortener.repository.UrlRepository;
import com.learning.urlshortener.util.CodeGenerator;
import org.springframework.stereotype.Service;


@Service
public class UrlService {

    private final CodeGenerator codeGenerator;
    private final UrlRepository urlRepository;

    public UrlService(CodeGenerator codeGenerator, UrlRepository urlRepository) {
        this.codeGenerator = codeGenerator;
        this.urlRepository = urlRepository;
    }

    public String getShortCode(String url) {

        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "https://" + url;
        }
        String shortCode =  codeGenerator.generateCode(6);

        UrlMapping urlMapping = new UrlMapping();

        urlMapping.setShortCode(shortCode);
        urlMapping.setOriginalUrl(url);
        urlMapping.setCreatedTime(System.currentTimeMillis());

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        long expirationTime = currentTimeInSeconds + (365 * 24 * 60 * 60);
        urlMapping.setExpiresAt(expirationTime);

        urlRepository.save(urlMapping);

        return shortCode;
    }

    public String getYourOriginalUrl(String shortCode) {
        UrlMapping urlMapping = urlRepository.getUrl(shortCode);

        if(urlMapping == null){
            throw new RuntimeException("Short code " + shortCode + " not found");
        }
        urlRepository.incrementClickCount(shortCode);
        return urlMapping.getOriginalUrl();
    }

    public UrlMapping getMapping(String shortCode) {
        UrlMapping urlMapping = urlRepository.getUrl(shortCode);
        if(urlMapping == null){
            throw new RuntimeException("Short code " + shortCode + " not found");
        }
        return urlMapping;
    }

}
