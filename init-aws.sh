#!/bin/bash
echo "Configurando SNS e SQS no LocalStack..."

# 1. Cria o Tópico (Antena)
aws --endpoint-url=http://localhost:4566 sns create-topic --name order-created-topic --region us-east-1

# 2. Cria a Fila (Caixa de Correio do Payment Service)
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name payment-queue --region us-east-1

# 3. Inscreve a Fila no Tópico (Liga a Caixa de Correio na Antena)
aws --endpoint-url=http://localhost:4566 sns subscribe \
    --topic-arn arn:aws:sns:us-east-1:000000000000:order-created-topic \
    --protocol sqs \
    --notification-endpoint arn:aws:sqs:us-east-1:000000000000:payment-queue \
    --region us-east-1

# 4. Inventory Queue
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name inventory-queue --region us-east-1
aws --endpoint-url=http://localhost:4566 sns subscribe \
    --topic-arn arn:aws:sns:us-east-1:000000000000:order-created-topic \
    --protocol sqs \
    --notification-endpoint arn:aws:sqs:us-east-1:000000000000:inventory-queue \
    --region us-east-1

# 5. Notification Queue
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name notification-queue --region us-east-1
aws --endpoint-url=http://localhost:4566 sns subscribe \
    --topic-arn arn:aws:sns:us-east-1:000000000000:order-created-topic \
    --protocol sqs \
    --notification-endpoint arn:aws:sqs:us-east-1:000000000000:notification-queue \
    --region us-east-1

# 6. DynamoDB Idempotency Table
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name ProcessedEvents \
    --attribute-definitions AttributeName=messageId,AttributeType=S \
    --key-schema AttributeName=messageId,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST \
    --region us-east-1

# 7. DynamoDB Idempotency Tables (Spring Cloud AWS naming fallback)
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name processedEventEntity \
    --attribute-definitions AttributeName=messageId,AttributeType=S \
    --key-schema AttributeName=messageId,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST \
    --region us-east-1

aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name processed_event_entity \
    --attribute-definitions AttributeName=messageId,AttributeType=S \
    --key-schema AttributeName=messageId,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST \
    --region us-east-1

echo "SNS, SQS e DynamoDB configurados com sucesso no LocalStack!"
