# Sistema de Loja de Materiais de Construção

Este é o projeto final para o sistema de gerenciamento de uma loja de materiais de construção, desenvolvido com Spring Boot e Java.

## 🛠️ Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot 3.2.3**
*   **Maven**
*   **PostgreSQL** (Banco de dados)
*   **Spring Data JPA** (Persistência)

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:

1.  [Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
2.  [Maven](https://maven.apache.org/download.cgi)
3.  [PostgreSQL](https://www.postgresql.org/download/)

## ⚙️ Configuração do Banco de Dados (PostgreSQL)

Você precisa criar um banco de dados para a aplicação se conectar.

1.  Abra o seu terminal do PostgreSQL (psql) ou uma ferramenta visual como PgAdmin/DBeaver.
2.  Execute o seguinte comando SQL para criar o banco:

```sql
CREATE DATABASE loja_construcao;
```

3.  Configure as credenciais no projeto. Abra o arquivo `src/main/resources/application.properties` e verifique (ou adicione) as seguintes linhas, alterando `seu_usuario` e `sua_senha` conforme sua instalação local do Postgres:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/loja_construcao
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> **Nota:** A configuração `ddl-auto=update` fará com que o Hibernate crie as tabelas (como `VendasItens`) automaticamente ao rodar o projeto.

## 🚀 Como Rodar o Projeto

1.  Clone este repositório:
    ```bash
    git clone https://github.com/seu-usuario/seu-repositorio.git
    ```

2.  Acesse a pasta do projeto:
    ```bash
    cd projeto-final-loja-de-materiais-de-construcao
    ```

3.  Execute o projeto via Maven:
    ```bash
    mvn spring-boot:run
    ```

Se tudo der certo, a aplicação iniciará e estará pronta para receber requisições (geralmente na porta `8080`).

## 🧪 Testes

Para rodar os testes automatizados:
```bash
mvn test
```
