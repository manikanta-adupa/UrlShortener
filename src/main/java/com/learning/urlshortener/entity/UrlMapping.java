package com.learning.urlshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class UrlMapping {
    private String shortCode;
    private String originalUrl;
    private long createdTime;

    @DynamoDbPartitionKey
    public String getShortCode() {
        return shortCode;
    }
}
