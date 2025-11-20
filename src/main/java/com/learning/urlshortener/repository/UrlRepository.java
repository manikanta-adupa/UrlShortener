package com.learning.urlshortener.repository;

import com.learning.urlshortener.entity.UrlMapping;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Repository
public class UrlRepository {
    private final DynamoDbTable<UrlMapping> table;
    public UrlRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table= dynamoDbEnhancedClient.table("UrlShortener", TableSchema.fromBean(UrlMapping.class));
    }
    public void save(UrlMapping urlMapping) {
        table.putItem(urlMapping);
    }
    public UrlMapping getUrl(String shortCode) {
        Key key = Key.builder().partitionValue(shortCode).build();
        return table.getItem(key);
    }

}
