package com.learning.urlshortener.service;

import com.learning.urlshortener.entity.UrlMapping;
import com.learning.urlshortener.repository.UrlRepository;
import com.learning.urlshortener.util.CodeGenerator;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;


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

        int maxTries = 5;
        for (int i = 1; i <= maxTries; i++) {
            String shortCode =  codeGenerator.generateCode(6);

            UrlMapping urlMapping = new UrlMapping();

            urlMapping.setShortCode(shortCode);
            urlMapping.setOriginalUrl(url);
            urlMapping.setCreatedTime(System.currentTimeMillis());

            long currentTimeInSeconds = System.currentTimeMillis() / 1000;
            long expirationTime = currentTimeInSeconds + (7 * 24 * 60 * 60);
            urlMapping.setExpiresAt(expirationTime);

            try {
                urlRepository.save(urlMapping);
                return shortCode;

            } catch (ConditionalCheckFailedException e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException("Maximum tries reached");
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
