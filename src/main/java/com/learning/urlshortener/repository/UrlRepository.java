package com.learning.urlshortener.repository;

import com.learning.urlshortener.entity.UrlMapping;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.Map;

@Repository
public class UrlRepository {

    private final DynamoDbTable<UrlMapping> table;
    private final DynamoDbClient rawClient;

    public UrlRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, DynamoDbClient rawClient) {
        this.table = dynamoDbEnhancedClient.table("UrlShortener", TableSchema.fromBean(UrlMapping.class));
        this.rawClient = rawClient;
    }

    public void save(UrlMapping urlMapping) {
        table.putItem(urlMapping);
    }

    public UrlMapping getUrl(String shortCode) {
        Key key = Key.builder().partitionValue(shortCode).build();
        return table.getItem(key);
    }

    public void incrementClickCount(String shortCode) {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName("UrlShortener")
                .key(Map.of("shortCode", AttributeValue.builder().s(shortCode).build()))
                .updateExpression("SET clickCount = if_not_exists(clickCount, :start) + :inc")
                .expressionAttributeValues(Map.of(
                        ":start", AttributeValue.builder().n("0").build(),
                        ":inc", AttributeValue.builder().n("1").build()
                ))
                .build();

        rawClient.updateItem(request);
    }
}