package com.example.loja.e_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "pedidoTaskExecutor")
    public Executor pedidoTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Quantidade de threads fixas que sempre ficam abertas esperando trabalho
        executor.setCorePoolSize(5);

        // Quantidade máxima de threads que o pool pode expandir caso a demanda estoure
        executor.setMaxPoolSize(10);

        // Capacidade da fila de espera interna caso todas as 10 threads estejam ocupadas
        executor.setQueueCapacity(500);

        // Prefixo para identificar as threads nos logs da aplicação (Excelente para debug!)
        executor.setThreadNamePrefix("ThreadPedido-");

        executor.initialize();
        return executor;
    }
}