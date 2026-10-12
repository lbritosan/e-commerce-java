package com.example.loja.e_commerce.repository;

import com.example.loja.e_commerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O Spring Data gera automaticamente a query SQL: SELECT * FROM usuarios WHERE email = ?
    Optional<Usuario> findByEmail(String email);

}
