package com.example.loja.e_commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Garante que o nome não seja nulo nem composto apenas por espaços em branco
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "A descrição do produto é obrigatória.")
    private String descricao;

    // Garante que o preço não seja nulo e seja estritamente maior que zero
    @NotNull(message = "O preço do produto é obrigatório.")
    @Positive(message = "O preço deve ser um valor maior que zero.")
    private BigDecimal preco;

    // --- CONSTRUTORES ---
    public Produto() {
    }

    // --- GETTERS E SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

}
