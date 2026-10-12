# 🏛️ Especificação Arquitetural de Alta Performance - E-Commerce Distribuído com Spring

Este documento especifica a arquitetura distribuída, orientada a eventos (*Event-Driven Architecture*) e concorrente do nosso ecossistema de e-commerce, construída de ponta a ponta utilizando o ecossistema robusto do **Spring Boot**.

---

## 🗺️ Visão Geral do Ecossistema Distribuído

## 🗺️ Visão Geral do Ecossistema Distribuído

```text
  [Cliente]
      │
      ▼ (HTTP POST /api/pedidos [Status 202 Accepted] - Síncrono)
┌─────────────────────────────────┐
│        Pedido-Service           │ (Spring Boot / Port: 8080)
│  ┌───────────────────────────┐  │
│  │    PedidoController       │  │ --> Endpoint de Entrada Isolado
│  └─────────────┬─────────────┘  │
│                │ (Injeção de Dependência)
│                ▼
│  ┌───────────────────────────┐  │
│  │ ThreadPoolTaskExecutor    │  │ --> Execução Concorrente (ThreadPedido-X)
│  └─────────────┬─────────────┘  │
└────────────────┼────────────────┘
                 │
                 ▼ (Mensagem AMQP - Assíncrono para Sprint 3)
       ┌───────────────────┐
       │  RabbitMQ Broker  │ 
       └───────────────────┘
```

---

## 🛠️ Stack Tecnológica Avançada e Justificativas de Engenharia

### 1. Processamento Assíncrono Interno: Spring Multithreading (`@Async`)
* **Justificativa:** Para maximizar o *throughput* (vazão) da API e reduzir o tempo de resposta percebido pelo cliente, tarefas pesadas de background que não bloqueiam o fluxo de compra (como geração de relatórios internos ou auditorias) são delegadas a um `ThreadPoolTaskExecutor` gerenciado pelo Spring. O cliente recebe um status imediato (`202 Accepted`), enquanto as threads secundárias processam em paralelo.

### 2. Mensageria Assíncrona: Spring AMQP & RabbitMQ
* **Justificativa:** O uso do RabbitMQ com o starter `spring-boot-starter-amqp` garante que o serviço de compras (`pedido-service`) não caia ou fique lento se o sistema de notificações ou gateways de terceiros ficarem instáveis. O Spring injeta o `RabbitTemplate` para postar mensagens e o `@RabbitListener` para consumi-las de forma assíncrona e resiliente.

### 3. Banco de Dados Relacional e Persistência: PostgreSQL
* **Justificativa:** O **PostgreSQL** garante isolamento transacional estrito (ACID) para manipulação de pedidos e estoque. A infraestrutura local roda isolada em containers Docker, conectada de forma transparente à aplicação via propriedades dinâmicas do Spring Boot, preparando a API para migração para o **Google Cloud SQL**.
