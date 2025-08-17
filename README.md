Gerenciador de Ciclo de Vida de Solicitações de Apólice de Seguro ACME
======================================================================

Este projeto é um microsserviço com arquitetura orientada a eventos (EDA) desenvolvido para gerenciar o ciclo de vida de solicitações de apólice de seguro da empresa ACME. A aplicação expõe uma API RESTful para criação e consulta de solicitações e utiliza o Apache Kafka para processamento assíncrono e gerenciamento de status.

O foco principal do projeto é demonstrar boas práticas de desenvolvimento, arquitetura, testes e observabilidade em um ecossistema moderno com Spring Boot.

💻 Tecnologias Utilizadas
-------------------------

*   **Java 17**: Linguagem principal da aplicação.

*   **Spring Boot 3.2.5**: Framework principal para o desenvolvimento do microsserviço.

*   **Maven**: Gerenciador de dependências e build.

*   **Apache Kafka**: Plataforma de streaming de eventos para comunicação assíncrona.

*   **PostgreSQL**: Banco de dados relacional para persistência dos dados de solicitações.

*   **Flyway**: Gerenciador de migrações de banco de dados.

*   **Lombok**: Geração automática de boilerplate code (getters, setters, etc).

*   **SpringDoc OpenAPI**: Geração de documentação da API (Swagger UI).

*   **Docker & Docker Compose**: Orquestração do ambiente de desenvolvimento local.

*   **WireMock**: Mock de API externa para testes de integração.

*   **Micrometer, Prometheus & Grafana**: Solução de observabilidade para métricas.

*   **Zipkin**: Ferramenta de rastreabilidade (distributed tracing).

*   **AKHQ (Kafka HQ)**: Ferramenta de monitoramento e gerenciamento para o Apache Kafka.


📊 Visão Geral da Arquitetura
-----------------------------

A arquitetura do projeto é baseada em microsserviços e segue um padrão de **Arquitetura Orientada a Eventos (EDA)**. A aplicação atua como um dos componentes centrais, processando o ciclo de vida das solicitações.

*   **Idempotência**: O endpoint de criação de propostas utiliza uma x-idempotency-key para garantir que a mesma solicitação não seja processada mais de uma vez, prevenindo efeitos colaterais indesejados.

*   **Processamento Assíncrono**: Ao receber uma nova solicitação, a API retorna uma resposta inicial e, de forma assíncrona, delega a validação de fraude a um serviço externo (simulado por WireMock). O status da solicitação é então atualizado no banco de dados.

*   **Observabilidade**: O projeto é instrumentado com Micrometer para coletar métricas e traces. As métricas são enviadas para o Prometheus e visualizadas em dashboards no Grafana. O Zipkin é utilizado para rastrear o fluxo de requisições através dos serviços, vital para diagnosticar problemas em arquiteturas distribuídas.

*   **Testabilidade**: A arquitetura foi pensada para ser facilmente testável. O WireMock atua como um simulador da API de fraude, permitindo que os testes de integração sejam executados de forma confiável, sem depender da disponibilidade de outros serviços.


### 📥 Tópicos e Payloads Kafka

A comunicação entre serviços é feita através de eventos publicados e consumidos nos seguintes tópicos:

**payments-events**Este tópico recebe eventos relacionados ao status de pagamento.
```json
{    "orderId": "5e13d10a-31a8-444b-9c76-8f35c5c0d500",    "status": "APROVADO",    "paymentMethod": "CARTAO_CREDITO",    "amount": 500.00  }  
```
**insurance-subscriptions-events**Este tópico é utilizado para eventos de subscrição da apólice, indicando que a apólice foi criada com sucesso.

```json
  {    "orderId": "5e13d10a-31a8-444b-9c76-8f35c5c0d500",    "status": "APROVADO",    "subscriptionId": "sub-123456789",    "policyNumber": "P001-2023-9999"  }   
```

🚀 Como Rodar o Projeto Localmente
----------------------------------

Siga os passos abaixo para ter a aplicação completa rodando na sua máquina.

### Pré-requisitos

*   **JDK 17**

*   **Maven**

*   **Docker & Docker Compose**


### Passo 1: Iniciar a Infraestrutura com Docker Compose

Abra um terminal na raiz do projeto e inicie todos os serviços de infraestrutura (PostgreSQL, Kafka, AKHQ, Prometheus, Grafana, etc.) com o Docker Compose.

```bash
 docker-compose up -d   
```

Este comando irá baixar as imagens (se necessário) e iniciar os contêineres em segundo plano.

### Passo 2: Construir e Iniciar a Aplicação Spring Boot

Com os serviços Docker rodando, compile a aplicação e inicie-a diretamente pela sua IDE (como IntelliJ ou VS Code) ou via linha de comando.
```bash
./mvnw clean package
```

```bash
java -jar target/acme.jar
```

A aplicação irá se conectar automaticamente aos serviços Docker através do endereço especial host.docker.internal.

🌐 Interfaces de Monitoramento e Documentação
---------------------------------------------

Após a sua aplicação e os containers estarem rodando, você pode acessar as seguintes interfaces no seu navegador para monitoramento e documentação.

*   **Swagger UI (Documentação da API)**: http://localhost:8080/acme/swagger-ui.html

*   **AKHQ (Monitoramento Kafka)**: http://localhost:8082

*   **Prometheus (Métricas)**: http://localhost:9090

*   **Grafana (Dashboards)**: http://localhost:3000

*   **Zipkin (Rastreabilidade/Traces)**: http://localhost:9411

*   **PGAdmin (Gerenciador de BD)**: http://localhost:5000

*   **WireMock (Mock API)**: http://localhost:8081


🛠️ Como Usar a API (via cURL)
------------------------------

Após iniciar a aplicação e os containers, você pode interagir com a API usando as seguintes chamadas curl. Certifique-se de que a aplicação está rodando na porta 8080.

### 1\. Criar uma Nova Proposta

Este endpoint cria uma nova solicitação de seguro. A chave x-idempotency-key é usada para garantir que a mesma solicitação não seja processada múltiplas vezes.
```bash
Pcurl --request POST \    --url http://localhost:8080/acme/v1/propostas \    --header 'Content-Type: application/json' \    --header 'x-idempotency-key: 1a75c2d6-effd-445d-aa0d-737ef5a0d703' \    --data '{    "customerId": "c27461c4-8d50-4a31-a4a3-25e98f003c88",    "productId": "fb1f3d4e-c6f3-46b2-832e-8a3a11a93406",    "category": "auto",    "salesChannel": "mobile",    "paymentMethod": "cartao_credito",    "totalMonthlyPremiumAmount": 75.25,    "insuredAmount": 275000.50,    "coverages": {      "Roubo": 100000.25,      "Perda Total": 10,      "Colisão com Terceiros": 75000.00    },    "assistances": [      "Guincho até 250km",      "Troca de Óleo",      "Chaveiro 24h"    ]  }'   
```

### 2\. Recuperar uma Proposta Específica

Para buscar os detalhes de uma proposta, utilize o ID único da proposta. Substitua 7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2 pelo ID da sua proposta.
```bash
 curl --request GET \    --url http://localhost:8080/acme/v1/propostas/7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2   
```

### 3\. Recuperar Propostas por Cliente

Busca todas as propostas associadas a um ID de cliente específico. Substitua 0b30b117-bea4-40db-89dc-3cb7d175e488 pelo ID do cliente que você deseja buscar.
```bash
 curl --request GET \    --url http://localhost:8080/acme/v1/propostas/cliente/0b30b117-bea4-40db-89dc-3cb7d175e488   
```

### 4\. Cancelar uma Proposta

Para alterar o status de uma proposta para "cancelada", use o endpoint PATCH e informe o ID da proposta. Substitua 7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2 pelo ID da sua proposta.
```bash
  curl --request PATCH \    --url http://localhost:8080/acme/v1/propostas/7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2/cancelar   
```