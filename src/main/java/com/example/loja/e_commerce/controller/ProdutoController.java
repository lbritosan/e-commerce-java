package com.example.loja.e_commerce.controller;


import com.example.loja.e_commerce.model.Produto;
import com.example.loja.e_commerce.service.ProdutoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> listar(){
        return produtoService.listarTodos();
    }

    @PostMapping
    public Produto criar(@Valid @RequestBody Produto produto){
        return produtoService.salvar(produto);
    }

}
