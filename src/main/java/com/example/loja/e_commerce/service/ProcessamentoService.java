package com.example.loja.e_commerce.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProcessamentoService {

    // Diz ao Spring para rodar este método no pool de threads que configuramos
    @Async("pedidoTaskExecutor")
    public void processarValidacaoPesadaDoPedido(Long pedidoId) {
        String threadNome = Thread.currentThread().getName();
        System.out.println("⏳ [" + threadNome + "] Iniciando validações assíncronas do pedido #" + pedidoId);

        try {
            // Simula um processamento pesado travando a thread secundária por 3 segundos
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("✅ [" + threadNome + "] Validações concluídas com sucesso para o pedido #" + pedidoId);
    }
}