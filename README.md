# Gerenciador de Ciclo de Vida de Solicitações de Apólice de Seguro ACME

Este projeto é um microsserviço com arquitetura orientada a eventos (EDA) desenvolvido para gerenciar o ciclo de vida de solicitações de apólice de seguro da empresa ACME.

A aplicação expõe uma API RESTful para criação e consulta de solicitações e utiliza o Apache Kafka para processamento assíncrono e gerenciamento de status.

O foco principal do projeto é demonstrar boas práticas de desenvolvimento, arquitetura, testes e observabilidade em um ecossistema moderno com Spring Boot.

## 💻 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Maven
- Apache Kafka
- PostgreSQL
- Flyway
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- Docker & Docker Compose
- WireMock
- Micrometer, Prometheus & Grafana
- Zipkin
- AKHQ (Kafka HQ)

## 📊 Visão Geral da Arquitetura

- **Idempotência:** endpoint de criação usa `x-idempotency-key` para evitar duplicidade.
- **Processamento Assíncrono:** delega verificação de fraude para serviço externo (mockado com WireMock).
- **Observabilidade:** métricas com Prometheus + Grafana, tracing com Zipkin.
- **Testabilidade:** arquitetura preparada para testes de integração com mocks externos.

## 📥 Tópicos e Payloads Kafka

A comunicação entre serviços é feita através de eventos nos tópicos Kafka.

### payments-events

Eventos de pagamento:

```json
{
  "orderId": "5e13d10a-31a8-444b-9c76-8f35c5c0d500",
  "status": "APROVADO",
  "paymentMethod": "CARTAO_CREDITO",
  "amount": 500.00
}
```

### insurance-subscriptions-events

Eventos de subscrição de apólice:

```json
{
  "orderId": "5e13d10a-31a8-444b-9c76-8f35c5c0d500",
  "status": "APROVADO",
  "subscriptionId": "sub-123456789",
  "policyNumber": "P001-2023-9999"
}
```

✅ Os tópicos já estão criados e você pode publicar eventos de teste diretamente pelo AKHQ.

## 🚀 Como Rodar o Projeto Localmente

A execução é 100% via Docker Compose, não sendo necessário rodar a aplicação manualmente pela IDE.

### Pré-requisitos

- Docker & Docker Compose

### Passo 1: Subir a aplicação e infraestrutura

Na raiz do projeto, execute:

```bash
docker-compose up -d
```

Esse comando vai:

- Criar e iniciar o container da aplicação ACME.
- Subir PostgreSQL, Kafka, AKHQ, Prometheus, Grafana, Zipkin, PGAdmin e WireMock.

💡 Não é necessário rodar `mvn package` ou `java -jar`. A imagem da aplicação já será construída e iniciada automaticamente.

## 🌐 Interfaces de Monitoramento e Documentação

- Swagger UI (API) → [http://localhost:8080/acme/swagger-ui.html](http://localhost:8080/acme/swagger-ui.html)
- AKHQ (Kafka) → [http://localhost:8082](http://localhost:8082)
- Prometheus (Métricas) → [http://localhost:9090](http://localhost:9090)
- Grafana (Dashboards) → [http://localhost:3000](http://localhost:3000)
- Zipkin (Traces) → [http://localhost:9411](http://localhost:9411)
- PGAdmin (Banco de Dados) → [http://localhost:5000](http://localhost:5000)
- WireMock (Mock de API) → [http://localhost:8081](http://localhost:8081)

## 🛠️ Como Usar a API (via cURL)

### 1. Criar uma Nova Proposta

```bash
curl --request POST \
  --url http://localhost:8080/acme/v1/propostas \
  --header 'Content-Type: application/json' \
  --header 'x-idempotency-key: 1a75c2d6-effd-445d-aa0d-737ef5a0d703' \
  --data '{
    "customerId": "c27461c4-8d50-4a31-a4a3-25e98f003c88",
    "productId": "fb1f3d4e-c6f3-46b2-832e-8a3a11a93406",
    "category": "auto",
    "salesChannel": "mobile",
    "paymentMethod": "cartao_credito",
    "totalMonthlyPremiumAmount": 75.25,
    "insuredAmount": 275000.50,
    "coverages": {
      "Roubo": 100000.25,
      "Perda Total": 10,
      "Colisão com Terceiros": 75000.00
    },
    "assistances": [
      "Guincho até 250km",
      "Troca de Óleo",
      "Chaveiro 24h"
    ]
  }'
```

### 2. Recuperar uma Proposta

```bash
curl --request GET \
  --url http://localhost:8080/acme/v1/propostas/{propostaId}
```

### 3. Buscar Propostas por Cliente

```bash
curl --request GET \
  --url http://localhost:8080/acme/v1/propostas/cliente/{clienteId}
```

### 4. Cancelar uma Proposta

```bash
curl --request PATCH \
  --url http://localhost:8080/acme/v1/propostas/{propostaId}/cancelar
```

👉 Agora, com apenas `docker-compose up -d`, você sobe todo o ecossistema + aplicação e pode testar os consumers pelo AKHQ.

