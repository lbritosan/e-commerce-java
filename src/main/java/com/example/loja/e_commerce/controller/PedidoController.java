package com.example.loja.e_commerce.controller;

import com.example.loja.e_commerce.service.ProcessamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final ProcessamentoService processamentoService;

    // Injeta o serviço assíncrono de forma limpa via DI
    public PedidoController(ProcessamentoService processamentoService) {
        this.processamentoService = processamentoService;
    }

    @PostMapping
    public ResponseEntity<?> criarPedidoFake() {
        Long pedidoFakeId = (long) (Math.random() * 10000);

        // Dispara o processamento pesado no Pool de Threads em segundo plano
        processamentoService.processarValidacaoPesadaDoPedido(pedidoFakeId);

        // Responde imediatamente para o cliente com o status 202 Accepted (Aceito para Processamento)
        System.out.println("🚀 [" + Thread.currentThread().getName() + "] HTTP aceito. Liberando o cliente...");

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "mensagem", "Pedido recebido com sucesso! O processamento seguirá em segundo plano.",
                        "pedidoId", pedidoFakeId
                ));
    }
}