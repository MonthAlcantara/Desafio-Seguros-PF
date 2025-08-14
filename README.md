# Gerenciador de Ciclo de Vida de Solicitações de apólice de Seguro ACME

Este projeto é um gerenciador de ciclo de vida para solicitações de apólice de seguro da empresa ACME, construído com Spring Boot. A aplicação expõe uma API RESTful que permite criar e consultar solicitações, além de gerenciar seu status.

O foco principal do projeto é a demonstração de boas práticas de desenvolvimento, arquitetura, testes e observabilidade em um ecossistema moderno com Spring Boot.

---

### 💻 Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.2.5**: Framework principal da aplicação.
* **Maven**: Gerenciador de dependências e build.
* **PostgreSQL**: Banco de dados relacional.
* **Flyway**: Gerenciador de migrações de banco de dados.
* **Lombok**: Geração automática de boilerplate code (getters, setters, etc).
* **MapStruct**: Geração de mappers para DTOs.
* **SpringDoc OpenAPI**: Geração de documentação da API (Swagger UI).
* **Docker & Docker Compose**: Orquestração do ambiente de desenvolvimento.
* **WireMock**: Mock de API externa para testes.
* **Micrometer, Prometheus & Grafana**: Solução de observabilidade para métricas.

---

### 🚀 Pré-requisitos

Para rodar o projeto localmente, você precisa ter as seguintes ferramentas instaladas:

* **JDK 17**
* **Maven**
* **Docker & Docker Compose**

---

### 🔧 Como Rodar o Projeto Localmente

Siga os passos abaixo para ter a aplicação completa rodando na sua máquina.

#### 1. Construir a Aplicação

Abra um terminal na raiz do projeto e compile a aplicação com o Maven. Isso garantirá que todas as dependências estejam baixadas.

```bash
mvn clean install
```

#### 2. Subir a Infraestrutura com Docker
Ainda no terminal, utilize o Docker Compose para iniciar os serviços de infraestrutura (PostgreSQL, PGAdmin, Flyway, Prometheus, Grafana e WireMock).

```bash
docker-compose up -d
```

O comando ```-d``` fará com que os containers rodem em segundo plano.

#### 3. Iniciar a Aplicação Spring Boot
Com os serviços Docker rodando, você pode iniciar a sua aplicação Spring Boot diretamente pela sua IDE (como IntelliJ ou VS Code).

Execute a classe principal ```AcmeApplication.java```.

A aplicação irá se conectar aos serviços Docker através do endereço especial ```host.docker.internal```.

🌐 Acessando as Interfaces Gráficas
Após a sua aplicação e os containers estarem rodando, você pode acessar as seguintes interfaces no seu navegador.

- Swagger UI (Documentação da API): ```http://localhost:8080/acme/swagger-ui.html```

- Prometheus (Métricas): ```http://localhost:9090```

- Grafana (Dashboards): ```http://localhost:3000```

- PGAdmin (Gerenciador de BD): ```http://localhost:5000```

- WireMock (Mock API):: ```http://localhost:8081```

### 📋 Visão Geral da Arquitetura e Componentes
A arquitetura do projeto é baseada em microsserviços e utiliza ferramentas para garantir um ciclo de vida robusto.

Banco de Dados (PostgreSQL): É o banco de dados principal da aplicação. O Flyway gerencia as migrações de esquema, garantindo que o banco de dados esteja sempre na versão correta.

Mocks de API (WireMock): A aplicação simula uma API externa de verificação de fraudes. O WireMock é um servidor de mock que atende em ```http://localhost:8081```, permitindo que o projeto funcione independentemente da disponibilidade da API real.

Documentação da API (SpringDoc): A aplicação usa anotações para gerar automaticamente a documentação no formato OpenAPI, que pode ser visualizada no Swagger UI.

Observabilidade (Prometheus & Grafana): O Micrometer coleta métricas automáticas da aplicação (JVM, HTTP, etc.), que são expostas no endpoint ```/acme/actuator/prometheus```. O Prometheus coleta essas métricas, e o Grafana é utilizado para criar dashboards visuais e alertas em tempo real.

### 📊 Acessando o Banco de Dados com PGAdmin
Você pode usar o PGAdmin para visualizar e gerenciar o banco de dados.

Acesse ```http://localhost:5000``` e faça login com as credenciais padrão do docker-compose: Email: ```admin@admin.com``` e Senha: ```admin```.

Adicione um novo servidor de banco de dados com as seguintes informações:

Host name/address: ```host.docker.internal```

Port: ```5432```

Maintenance database: ```acme```

Username: ```acme_user```

Password: ```acme_pass```

### 🕵️‍♂️ Acessando o Mock de API com WireMock
Você pode ver o status do WireMock em ```http://localhost:8081```. A API mockada simula o endpoint de verificação de fraude em GET ```/api/v1/fraud-check/{documento}``` e retorna um JSON para cada solicitação.