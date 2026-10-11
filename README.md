# 🛒 E-Commerce API - Catálogo de Produtos

[![Java Version](https://shields.io)](https://openjdk.org)
[![Spring Boot](https://shields.io)](https://spring.io)
[![Docker](https://shields.io)](https://docker.com)
[![GCP](https://shields.io)](https://google.com)

Esta é uma API REST funcional que representa o microsserviço de **Catálogo de Produtos** para uma loja virtual. O projeto foi desenvolvido com foco em alta portabilidade, segurança robusta e arquitetura nativa em nuvem (*cloud-native*), estando totalmente preparado para implantação em containers no **Google Cloud Platform (GCP)**.

---

## 🛠️ Tecnologias Utilizadas e Pilares Arquiteturais

O projeto foi construído utilizando o ecossistema Java moderno e adota os três pilares fundamentais de aplicações corporativas:

### 1. Injeção de Dependências (DI) & Camadas
* **Spring Boot (Core):** Utilizado para gerenciar o ciclo de vida dos componentes e desacoplar as regras de negócio através da Injeção de Dependências por construtor.
* **Arquitetura em Camadas:** Divisão clara de responsabilidades entre `Controller` (Exposição de Endpoints HTTP), `Service` (Regras de negócio e intermediação) e `Repository` (Acesso a dados).

### 2. Persistência de Dados
* **Spring Data JPA & Hibernate:** Abstração completa da camada de banco de dados, eliminando a necessidade de escrita manual de consultas SQL redundantes através do padrão *Repository*.
* **PostgreSQL:** Banco de dados relacional robusto e definitivo utilizado para garantir a persistência e integridade dos dados da loja em ambientes de desenvolvimento e produção.

### 3. Segurança (OAuth2 / JWT)
* **Spring Security:** Configurado para atuar estritamente como um **Resource Server** (Servidor de Recursos).
* **Autenticação Stateless com JWT:** A API valida localmente tokens digitais criptografados via assinatura HMAC SHA-256 (usando `JwtDecoder`), permitindo que a rota de listagem de produtos seja pública, enquanto operações de escrita exigem credenciais administrativas.

---

## 🐳 Containerização com Docker

A aplicação conta com um arquivo `Dockerfile` otimizado em múltiplos estágios (*Multi-stage Build*). Isso garante que o ambiente de compilação seja isolado e que a imagem final distribuída contenha apenas o estritamente necessário para a execução, reduzindo drasticamente o tamanho do container e a superfície de vulnerabilidades.

* **Fase 1 (Build):** Utiliza uma imagem do Maven com Java 21 para compilar o código e gerar o `.jar`.
* **Fase 2 (Runtime):** Utiliza uma imagem leve `eclipse-temurin:21-jre` apenas para rodar o binário gerado.

---

## ☁️ Implantação no Google Cloud Platform (GCP)

O projeto foi validado e implantado com sucesso no ecossistema do Google Cloud utilizando serviços modernos e escaláveis:
1. **Google Cloud Build:** Utilizado para ler o código-fonte e compilar a imagem Docker remotamente na infraestrutura do Google.
2. **Artifact Registry:** Repositório seguro e privado onde a imagem Docker gerada fica armazenada.
3. **Google Cloud Run:** Serviço *Serverless* que gerencia a execução do container. Ele fornece escalabilidade automática (inclusive reduzindo a zero containers ativos se não houver tráfego) e expõe a API sob uma URL segura criptografada com HTTPS.

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
* Java 21 instalado.
* Docker Desktop instalado e rodando.

### Opção 1: Executando via IDE (Com Banco de Dados no Docker)
1. Suba o container do banco de dados PostgreSQL via terminal:
   ```bash
   docker run --name loja-postgres -e POSTGRES_DB=lojadb -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres:15
   ```
2. Abra o projeto na sua IDE (IntelliJ ou VS Code).
3. Certifique-se de que as propriedades de conexão com o PostgreSQL estão ativas no `application.properties`.
4. Execute a classe principal anotada com `@SpringBootApplication`.
5. A API estará disponível em `http://localhost:8080`.

### Opção 2: Executando a Arquitetura Completa via Containers (Isolado da IDE)
1. Crie uma rede isolada no Docker para comunicação entre os containers:
   ```bash
   docker network create loja-network
   ```
2. Inicialize o container do banco de dados conectado à rede:
   ```bash
   docker run --name loja-postgres --network loja-network -e POSTGRES_DB=lojadb -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres:15
   ```
3. Gere o binário executável da aplicação limpando builds antigos:
   ```bash
   mvn clean package -DskipTests
   ```
4. Construa a imagem Docker da API:
   ```bash
   docker build -t e-commerce-api:v2 .
   ```
5. Inicie o container da aplicação injetando a URL de rede do banco de dados dinamicamente:
   ```bash
   docker run -d -p 8080:8080 --name loja-app-container --network loja-network -e SPRING_DATASOURCE_URL=jdbc:postgresql://loja-postgres:5432/lojadb e-commerce-api:v2
   ```

---

## 🗺️ Endpoints da API e Como Testar

### 1. Listar Produtos (Público)
* **Método:** `GET`
* **URL:** `http://localhost:8080/api/produtos`
* **Resposta esperada (Status 200 OK):** Um JSON contendo a lista de produtos cadastrados.

### 2. Cadastrar Produto (Protegido via Terminal/cURL)
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/produtos`
* **Comando de disparo seguro (HTTP/1.1):**
  ```bash
  curl -X POST http://localhost:8080/api/produtos \
    -H "Authorization: Bearer <SEU_TOKEN_JWT>" \
    -H "Content-Type: application/json" \
    -d '{"nome": "Mouse Vertical Ergonomico", "descricao": "Mouse sem fio anatomico", "preco": 189.90}'
  ```
* **Resposta esperada (Status 201 Created):** Retorno do JSON enviado, contendo o ID gerado pelo PostgreSQL.

### 3. Visualizar e Consultar o Banco de Dados PostgreSQL (Git Bash)
Para verificar de forma rápida os registros armazenados fisicamente no container do banco de dados sem a necessidade de um cliente visual externo, execute o comando direto abaixo:
```bash
docker exec -i loja-postgres psql -U admin -d lojadb -c "SELECT * FROM produtos;"
```

---

## 🔑 Geração de Token para Testes Locais
Para facilitar a validação da rota protegida durante o desenvolvimento, o projeto possui uma classe utilitária (`GeradorTokenUtil`) que implementa `CommandLineRunner`. Sempre que a aplicação inicializa, **um token JWT válido de testes é impresso automaticamente no console/log da IDE ou do container**, bastando copiá-lo e utilizá-lo no cabeçalho de autenticação do seu cliente HTTP.
