package com.shopflow.domain.payment;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
public class ProcessedEventRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    public ProcessedEventRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    public boolean existsById(String messageId) {
        Key key = Key.builder().partitionValue(messageId).build();
        ProcessedEventEntity entity = dynamoDbTemplate.load(key, ProcessedEventEntity.class);
        return entity != null;
    }

    public void save(ProcessedEventEntity entity) {
        dynamoDbTemplate.save(entity);
    }
}
