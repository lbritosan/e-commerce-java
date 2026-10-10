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
* **H2 Database:** Banco de dados relacional executado em memória (*in-memory*), ideal para ciclos de desenvolvimento ágil e testes locais rápidos sem dependências externas.

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

### Opção 1: Executando via IDE (Sem Docker)
1. Clone o repositório:
   ```bash
   git clone https://github.com
   ```
2. Abra o projeto na sua IDE (IntelliJ ou VS Code).
3. Execute a classe principal anotada com `@SpringBootApplication`.
4. A API estará disponível em `http://localhost:8080`.

### Opção 2: Executando via Docker
1. Construa a imagem localmente:
   ```bash
   docker build -t e-commerce-api:v1 .
   ```
2. Inicie o container mapeando as portas:
   ```bash
   docker run -d -p 8080:8080 --name loja-container e-commerce-api:v1
   ```

---

## 🗺️ Endpoints da API e Como Testar

### 1. Listar Produtos (Público)
* **Método:** `GET`
* **URL:** `http://localhost:8080/api/produtos`
* **Resposta esperada (Status 200 OK):** Um JSON contendo a lista de produtos cadastrados.

### 2. Cadastrar Produto (Protegido)
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/produtos`
* **Cabeçalho Obrigatório:** `Authorization: Bearer <SEU_TOKEN_JWT>`
* **Corpo da Requisição (JSON):**
  ```json
  {
      "nome": "Teclado Mecânico RGB",
      "descricao": "Switch Blue e iluminação customizável",
      "preco": 349.90
  }
  ```
* **Resposta esperada (Status 201 Created):** Retorno do JSON enviado, agora contendo o ID gerado pelo banco de dados.

### 3. Visualizar Banco de Dados H2
* **URL:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:lojadb`
* **User:** `sa` (Senha em branco)

---

## 🔑 Geração de Token para Testes Locais
Para facilitar a validação da rota protegida durante o desenvolvimento, o projeto possui uma classe utilitária (`GeradorTokenUtil`) que implementa `CommandLineRunner`. Sempre que a aplicação inicializa, **um token JWT válido de testes é impresso automaticamente no console/log da IDE**, bastando copiá-lo e colá-lo na aba *Authorization -> Bearer Token* do seu Postman ou Insomnia.
