# Gerenciador de Ciclo de Vida de Solicitações de Apólice de Seguro ACME

Este projeto é um microsserviço com arquitetura orientada a eventos (EDA) desenvolvido para gerenciar o ciclo de vida de solicitações de apólice de seguro da empresa ACME.

A aplicação expõe uma API RESTful para criação e consulta de solicitações e utiliza Apache Kafka para processamento assíncrono e gerenciamento de status.

O projeto demonstra boas práticas de desenvolvimento, arquitetura, testes e observabilidade com Spring Boot, contemplando documentação, monitoramento e integração de serviços.

## 💻 Tecnologias Utilizadas

- **Java 17:** linguagem principal, segura e robusta para microsserviços.
- **Spring Boot 3.2.5:** framework para criação rápida de aplicações RESTful e integração com Kafka.
- **Maven:** gerenciamento de dependências e build do projeto.
- **Apache Kafka:** backbone de eventos assíncronos, garantindo comunicação entre microsserviços.
- **PostgreSQL:** banco de dados relacional para armazenamento das propostas.
- **Flyway:** controle de versionamento de banco de dados e migrações.
- **Lombok:** simplifica a criação de getters, setters e builders.
- **SpringDoc OpenAPI (Swagger UI):** documentação interativa da API.
- **Docker & Docker Compose:** conteinerização e orquestração de toda a infraestrutura.
- **WireMock:** simulação de serviços externos, como verificação de fraude.
- **Micrometer, Prometheus & Grafana:** monitoramento e métricas de aplicação.
- **Zipkin:** tracing distribuído para rastrear chamadas entre serviços.
- **AKHQ (Kafka HQ):** interface para monitorar tópicos Kafka e mensagens.

## 📊 Visão Geral da Arquitetura

- **Idempotência:** endpoints de criação usam `x-idempotency-key` para evitar duplicidade.
- **Processamento Assíncrono:** delegação de verificações e processamento de eventos a serviços externos.
- **Consumers Kafka:** serviços consumidores processam tópicos específicos e atualizam estados das propostas.
- **Observabilidade:** métricas com Prometheus/Grafana e tracing com Zipkin para monitorar performance e detectar problemas.
- **Documentação:** APIs documentadas com Swagger/OpenAPI, permitindo avaliação clara do comportamento da aplicação.
- **Testabilidade:** estrutura preparada para testes unitários, integração e simulações de serviços externos.
- **Persistência e Versionamento:** PostgreSQL com Flyway garante consistência e histórico de alterações de dados.

## 🚀 Como Rodar o Projeto Localmente

### Pré-requisitos

- Docker & Docker Compose

### Passo 1: Subir aplicação e infraestrutura

```bash
docker-compose up -d
```

- Cria e inicia o container da aplicação ACME.
- Sobe PostgreSQL, Kafka, AKHQ, Prometheus, Grafana, Zipkin, PGAdmin e WireMock.

💡 Não é necessário `mvn package` ou `java -jar`. A imagem é construída automaticamente.

## 🌐 Interfaces de Monitoramento e Documentação

- Swagger UI → [http://localhost:8080/acme/swagger-ui.html](http://localhost:8080/acme/swagger-ui.html)
- AKHQ → [http://localhost:8082](http://localhost:8082)
- Prometheus → [http://localhost:9090](http://localhost:9090)
- Grafana → [http://localhost:3000](http://localhost:3000)
- Zipkin → [http://localhost:9411](http://localhost:9411)
- PGAdmin → [http://localhost:5000](http://localhost:5000)
- WireMock → [http://localhost:8081](http://localhost:8081)

## 🛠️ Como Usar a API (via cURL) e Eventos Kafka

### 1. Criar Nova Proposta

```bash
curl -X POST http://localhost:8080/acme/v1/propostas \
  -H 'Content-Type: application/json' \
  -H 'x-idempotency-key: 1a75c2d6-effd-445d-aa0d-737ef5a0d703' \
  -d '{
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
    "assistances": ["Guincho até 250km","Troca de Óleo","Chaveiro 24h"]
  }'
```

### 2. Tópicos e Eventos Kafka

- **payments-events:** eventos de pagamento processados pelos consumers.

```json
{
  "orderId": "5e13d10a-31a8-444b-9c76-8f35c5c0d500",
  "status": "APROVADO",
  "paymentMethod": "CARTAO_CREDITO",
  "amount": 500.00
}
```

- **insurance-subscriptions-events:** eventos de subscrição de apólice.

```json
{
  "orderId": "5e13d10a-31a8-444b-9c76-8f35c5c0d500",
  "status": "APROVADO",
  "subscriptionId": "sub-123456789",
  "policyNumber": "P001-2023-9999"
}
```

✅ Tópicos já criados; publique eventos de teste via AKHQ.

### 3. Recuperar Proposta

```bash
curl -X GET http://localhost:8080/acme/v1/propostas/{propostaId}
```

### 4. Buscar Propostas por Cliente

```bash
curl -X GET http://localhost:8080/acme/v1/propostas/cliente/{clienteId}
```

### 5. Cancelar Proposta

```bash
curl -X PATCH http://localhost:8080/acme/v1/propostas/{propostaId}/cancelar
```

👉 Com `docker-compose up -d`, todo o ecossistema + aplicação é iniciado e você pode testar os consumers pelo AKHQ.

