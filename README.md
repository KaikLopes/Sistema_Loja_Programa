# 🏗️ Sistema de Gestão - Loja de Materiais de Construção

Bem-vindo ao repositório do **Sistema de Loja de Materiais de Construção**. Este é uma aplicação Desktop desenvolvida em **Java**, combinando a robustez do **Spring Boot** no backend com uma interface gráfica moderna construída em **Swing** (utilizando a biblioteca **FlatLaf** para estilização).

O sistema oferece uma solução completa para gestão de estoque, vendas (PDV), cadastro de clientes, fornecedores e relatórios financeiros.

---

## 🚀 Funcionalidades

O sistema conta com um menu lateral responsivo e diversos módulos:

*   **📊 Dashboard:** Visão geral com cartões de faturamento, valor em estoque, contagem de vendas e gráfico de ranking de produtos mais vendidos.
*   **🛒 Ponto de Venda (PDV):** Interface para realização de novas vendas, cálculo de subtotal e baixa automática de estoque.
*   **📦 Gestão de Produtos:** Cadastro completo com controle de preço de compra/venda, quantidade e associação com Fornecedores e Categorias.
*   **👥 Gestão de Clientes:** Cadastro e manutenção da base de clientes.
*   **🚚 Gestão de Fornecedores:** Controle de parceiros comerciais.
*   **🏷️ Categorias:** Organização dos produtos por setores.
*   **💰 Relatórios:** Fluxo de caixa com filtros por período (Hoje, Semana, Mês, Ano) detalhando Entradas, Saídas e Lucro.
*   **👤 Perfil de Usuário:** Área para alteração de senha e dados cadastrais do operador logado.
*   **🔐 Autenticação:** Sistema de Login e Cadastro de novos usuários.

---

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java 21
*   **Framework:** Spring Boot 3.2.3
*   **Interface Gráfica:** Java Swing + FlatLaf (Look and Feel moderno)
*   **Banco de Dados:** PostgreSQL
*   **Persistência:** Spring Data JPA / Hibernate
*   **Gerenciador de Dependências:** Maven

---

## 📋 Pré-requisitos

Para rodar o projeto localmente, você precisará ter instalado:

1.  **[Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)**
2.  **[Maven](https://maven.apache.org/download.cgi)** (Geralmente incluído nas IDEs)
3.  **[PostgreSQL](https://www.postgresql.org/download/)**
4.  **[Git](https://git-scm.com/downloads)**


---

## ⚙️ Configuração do Banco de Dados

O projeto está configurado para conectar-se a um banco de dados PostgreSQL. Siga os passos abaixo para preparar o ambiente:

1.  Abra seu gerenciador de banco de dados (PgAdmin, DBeaver ou terminal `psql`).
2.  Crie o banco de dados e o usuário conforme as configurações do arquivo `application.properties`:

```sql
-- 1. Criar o banco de dados
CREATE DATABASE minha_loja;

-- 2. Criar o usuário (role) com a senha esperada pelo sistema
CREATE USER loja_user WITH ENCRYPTED PASSWORD 'loja10';

-- 3. Conceder permissões ao usuário no banco
GRANT ALL PRIVILEGES ON DATABASE minha_loja TO loja_user;

-- (Opcional) Se estiver usando PostgreSQL 15+, também execute:
-- \c minha_loja
-- GRANT ALL ON SCHEMA public TO loja_user;
