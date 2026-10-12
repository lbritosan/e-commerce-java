package com.example.loja.e_commerce.config;

import com.example.loja.e_commerce.model.Produto;
import com.example.loja.e_commerce.model.Role;
import com.example.loja.e_commerce.model.Usuario;
import com.example.loja.e_commerce.repository.ProdutoRepository;
import com.example.loja.e_commerce.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.util.Set;

@Configuration
public class DadosIniciaisConfig implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // O Spring injeta tudo via construtor (DI)
    public DadosIniciaisConfig(ProdutoRepository produtoRepository,
                               UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) {
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Cadastra Produtos se a tabela estiver vazia
        if (produtoRepository.count() == 0) {
            Produto p1 = new Produto();
            p1.setNome("Notebook Gamer");
            p1.setDescricao("Processador potente e placa de vídeo de última geração");
            p1.setPreco(new BigDecimal("5499.90"));

            Produto p2 = new Produto();
            p2.setNome("Smartphone 5G");
            p2.setDescricao("Tela OLED e câmera de 108 MP");
            p2.setPreco(new BigDecimal("2999.00"));

            produtoRepository.save(p1);
            produtoRepository.save(p2);
        }

        // 2. Cadastra Usuários de Teste se a tabela estiver vazia
        if (usuarioRepository.count() == 0) {
            // Criando o Administrador da loja
            Usuario admin = new Usuario();
            admin.setEmail("admin@loja.com");
            // Criptografa a senha "admin123" usando o BCrypt antes de salvar
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_CLIENTE));

            // Criando um Cliente comum da loja
            Usuario cliente = new Usuario();
            cliente.setEmail("cliente@loja.com");
            cliente.setSenha(passwordEncoder.encode("cliente123"));
            cliente.setRoles(Set.of(Role.ROLE_CLIENTE));

            usuarioRepository.save(admin);
            usuarioRepository.save(cliente);

            System.out.println("🚀 Usuários de teste (ADMIN e CLIENTE) criados com senhas criptografadas via BCrypt!");
        }
    }
}
