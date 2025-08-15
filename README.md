# Gerenciador de Ciclo de Vida de Solicitações de apólice de Seguro ACME

Este projeto é um gerenciador de ciclo de vida para solicitações de apólice de seguro da empresa ACME, construído com Spring Boot. A aplicação expõe uma API RESTful que permite criar e consultar solicitações, além de gerenciar seu status.

O foco principal do projeto é a demonstração de boas práticas de desenvolvimento, arquitetura, testes e observabilidade em um ecossistema moderno com Spring Boot.

-----

### 💻 Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.2.5**: Framework principal da aplicação.
* **Maven**: Gerenciador de dependências e build.
* **PostgreSQL**: Banco de dados relacional.
* **Flyway**: Gerenciador de migrações de banco de dados.
* **Lombok**: Geração automática de boilerplate code (getters, setters, etc).
* **SpringDoc OpenAPI**: Geração de documentação da API (Swagger UI).
* **Docker & Docker Compose**: Orquestração do ambiente de desenvolvimento.
* **WireMock**: Mock de API externa para testes.
* **Micrometer, Prometheus & Grafana**: Solução de observabilidade para métricas.

-----

### Visão Geral da Arquitetura

A arquitetura do projeto é baseada em microsserviços e segue o padrão de Arquitetura Orientada a Eventos (EDA). A aplicação atua como um dos componentes centrais deste ecossistema, processando o ciclo de vida das solicitações.

**Idempotência**: O endpoint de criação de propostas utiliza uma x-idempotency-key para garantir que a mesma solicitação não seja processada mais de uma vez. Essa é uma prática essencial em sistemas distribuídos para prevenir efeitos colaterais indesejados.

**Processamento Assíncrono**: Ao receber uma nova solicitação, a API retorna uma resposta inicial e, de forma assíncrona, delega a validação de fraude a um serviço externo (simulado pelo WireMock). O status da solicitação é então atualizado no banco de dados.

**Observabilidade**: O projeto é instrumentado com o Micrometer para coletar métricas e traces. As métricas são enviadas para o Prometheus e visualizadas em dashboards no Grafana. O Zipkin é utilizado para rastrear o fluxo de requisições através dos serviços, o que é vital para diagnosticar problemas em arquiteturas distribuídas.

**Testabilidade**: A arquitetura foi pensada para ser facilmente testável. O WireMock atua como um simulador da API de fraude, permitindo que os testes de integração do nosso microsserviço sejam executados de forma confiável, sem depender da disponibilidade de outros serviços.

### 🚀 Pré-requisitos

Para rodar o projeto localmente, você precisa ter as seguintes ferramentas instaladas:

* **JDK 17**
* **Maven**
* **Docker & Docker Compose**

-----

### 🔧 Como Rodar o Projeto Localmente

Siga os passos abaixo para ter a aplicação completa rodando na sua máquina.

#### 1\. Construir a Aplicação

Abra um terminal na raiz do projeto e compile a aplicação com o Maven. Isso garantirá que todas as dependências estejam baixadas.

```
mvn clean install
```

#### 2\. Subir a Infraestrutura com Docker

Ainda no terminal, utilize o Docker Compose para iniciar os serviços de infraestrutura (PostgreSQL, PGAdmin, Flyway, Prometheus, Grafana e WireMock).

```
docker-compose up -d
```

O comando `-d` fará com que os containers rodem em segundo plano.

#### 3\. Iniciar a Aplicação Spring Boot

Com os serviços Docker rodando, você pode iniciar a sua aplicação Spring Boot diretamente pela sua IDE (como IntelliJ ou VS Code).

Execute a classe principal `AcmeApplication.java`.

A aplicação irá se conectar aos serviços Docker através do endereço especial `host.docker.internal`.

🌐 **Acessando as Interfaces Gráficas**

Após a sua aplicação e os containers estarem rodando, você pode acessar as seguintes interfaces no seu navegador.

* **Swagger UI (Documentação da API)**: `http://localhost:8080/acme/swagger-ui.html`
* **Prometheus (Métricas)**: `http://localhost:9090`
* **Grafana (Dashboards)**: `http://localhost:3000`
* **Zipkin (Rastreabilidade/Traces)**: `http://localhost:9411`
* **PGAdmin (Gerenciador de BD)**: `http://localhost:5000`
* **WireMock (Mock API)**: `http://localhost:8081`

-----

### 🛠️ Como Usar a API (via cURL)

Após iniciar a aplicação e os containers, você pode interagir com a API usando as seguintes chamadas `curl`. Certifique-se de que a aplicação está rodando na porta `8080`.

#### 1\. Criar uma Nova Proposta

Este endpoint cria uma nova solicitação de seguro. Ele espera um corpo JSON com os detalhes da proposta. A chave `x-idempotency-key` é usada para garantir que a mesma solicitação não seja processada múltiplas vezes.

```
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

#### 2\. Recuperar uma Proposta Específica

Para buscar os detalhes de uma proposta, utilize o ID único da proposta no endpoint GET. Substitua `7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2` pelo ID da sua proposta.

```
curl --request GET \
  --url http://localhost:8080/acme/v1/propostas/7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2
```

#### 3\. Recuperar Propostas por Cliente

Busca todas as propostas associadas a um ID de cliente específico. Substitua `0b30b117-bea4-40db-89dc-3cb7d175e488` pelo ID do cliente que você deseja buscar.

```
curl --request GET \
  --url http://localhost:8080/acme/v1/propostas/cliente/0b30b117-bea4-40db-89dc-3cb7d175e488
```

#### 4\. Cancelar uma Proposta

Para alterar o status de uma proposta para "cancelada", use o endpoint PATCH e informe o ID da proposta. Substitua `7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2` pelo ID da sua proposta.

```
curl --request PATCH \
  --url http://localhost:8080/acme/v1/propostas/7095210f-107a-4b0d-9ed7-ba3d6a6d3bc2/cancelar
```

-----

### 📋 Visão Geral da Arquitetura e Componentes

A arquitetura do projeto é baseada em microsserviços e utiliza ferramentas para garantir um ciclo de vida robusto.

**Banco de Dados (PostgreSQL)**: É o banco de dados principal da aplicação. O Flyway gerencia as migrações de esquema, garantindo que o banco de dados esteja sempre na versão correta.

**Mocks de API (WireMock)**: A aplicação simula uma API externa de verificação de fraudes. O WireMock é um servidor de mock que atende em `http://localhost:8081`, permitindo que o projeto funcione independentemente da disponibilidade da API real.

**Documentação da API (SpringDoc)**: A aplicação usa anotações para gerar automaticamente a documentação no formato OpenAPI, que pode ser visualizada no Swagger UI.

**Observabilidade (Prometheus, Grafana & Zipkin):** O **Micrometer** coleta métricas e traces. As métricas são enviadas para o **Prometheus** e visualizadas no **Grafana** (agora com um dashboard pré-configurado). Os traces são enviados para o **Zipkin**.

### 📊 Acessando o Banco de Dados com PGAdmin

Você pode usar o PGAdmin para visualizar e gerenciar o banco de dados.

Acesse `http://localhost:5000` e faça login com as credenciais padrão do docker-compose: Email: `admin@admin.com` e Senha: `admin`.

Adicione um novo servidor de banco de dados com as seguintes informações:

* Host name/address: `host.docker.internal`
* Port: `5432`
* Maintenance database: `acme`
* Username: `acme_user`
* Password: `acme_pass`

### 🕵️‍♂️ Acessando o Mock de API com WireMock

Você pode ver o status do WireMock em `http://localhost:8081`. A API mockada simula o endpoint de verificação de fraude em GET `/api/v1/fraud-check/{documento}` e retorna um JSON para cada solicitação.