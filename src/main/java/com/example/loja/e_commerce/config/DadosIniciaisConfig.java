package com.example.loja.e_commerce.config;

import com.example.loja.e_commerce.model.Produto;
import com.example.loja.e_commerce.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class DadosIniciaisConfig implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;

    public DadosIniciaisConfig(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) throws Exception{

        Produto p1 = new Produto();
        p1.setNome("Notebook Gamer");
        p1.setDescricao("Processador potente e placa de vídeo de última geração");
        p1.setPreco(new BigDecimal("5499.90"));

        // Criando o segundo produto
        Produto p2 = new Produto();
        p2.setNome("Smartphone 5G");
        p2.setDescricao("Tela OLED e câmera de 108 MP");
        p2.setPreco(new BigDecimal("2999.00"));

        // Salvando no banco de dados H2
        produtoRepository.save(p1);
        produtoRepository.save(p2);

        System.out.println("🚀 Banco de dados H2 inicializado com sucesso com produtos de teste!");

    }
}
