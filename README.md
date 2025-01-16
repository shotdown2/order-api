# Sobre o Projeto

Este projeto é uma aplicação robusta para o controle de ordens, desenvolvida em **Java 17** com **Spring Boot**. A solução é projetada para atender às demandas de processamento de ordens com validações e integração de produtos. A aplicação utiliza **PostgreSQL** como banco de dados, integração com **Swagger** para documentação, e está contida em um ambiente **Docker** para facilitar a implantação.

## Arquitetura do Sistema

A solução adota o padrão Hexagonal Architecture (Ports and Adapters), garantindo maior desacoplamento e testabilidade. A organização segue três camadas principais:

1. **Application**: Contém os casos de uso e a orquestração da lógica de negócio. É a camada intermediária que conecta o domínio às interfaces externas.
2. **Domain**: Abriga as entidades e regras de negócio fundamentais. Essa camada é independente de frameworks e tecnologia.
3. **Infrastructure**: Implementa os adaptadores externos, como repositórios, APIs REST e configurações específicas do framework.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **PostgreSQL 17**
- **Docker**
- **Swagger**

## Principais Interações

1. **Criação de Ordens**:
    - O cliente envia uma lista de ordens para o endpoint `POST /orders`.
    - As ordens são validadas e processadas paralelamente.
    - As ordens são salvas no banco de dados, e os produtos associados às ordens são registrados.
    - O sistema retorna uma lista com o status do processamento.

2. **Consulta de Ordens**:
    - O endpoint `GET /orders` retorna uma página de ordens com detalhes como cliente, valor total e status.
    - É possível buscar ordens individuais pelo ID externo no endpoint `GET /orders/{externalOrderId}`.

---

## Links Importantes

- **Swagger UI**: A documentação da API pode ser acessada em [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/).
- **Api**: [http://localhost:8080](http://localhost:8080/home).

## Como Executar a Aplicação

### 1. Pré-requisitos

Certifique-se de que as seguintes ferramentas estão instaladas em sua máquina:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)

### 2. Configuração do Ambiente

Para executar a aplicação, você pode usar o comando:

```bash
docker-compose up --build
```