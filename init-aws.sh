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

echo "SNS e SQS configurados com sucesso no LocalStack!"
