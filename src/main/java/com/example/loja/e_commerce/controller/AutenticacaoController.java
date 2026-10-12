package com.example.loja.e_commerce.controller;

import com.example.loja.e_commerce.dto.LoginRequest;
import com.example.loja.e_commerce.model.Usuario;
import com.example.loja.e_commerce.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisTypeAnnos;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AutenticacaoController(UsuarioRepository pUsuarioRepository,
                                  PasswordEncoder pPasswordEncoder,
                                  JwtEncoder pJwtEncoder) {
        this.usuarioRepository = pUsuarioRepository;
        this.passwordEncoder = pPasswordEncoder;
        this.jwtEncoder = pJwtEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository
                            .findByEmail(loginRequest
                                .getEmail())
                                    .orElseThrow(()
                                        -> new RuntimeException("Usuário ou senha inválidos."));

        // 2. Valida se a senha informada condiz com o hash criptografado do banco
        if(!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body(Map.of("erro", "Usuário ou senha inválidos"));
        }

        // 3. Mapeia as Roles do usuário em uma única String separada por espaços (Ex: "ROLE_ADMIN ROLE_CLIENTE")
        String escopos = usuario.getRoles()
                                    .stream()
                                        .map(Enum::name)
                .collect(Collectors.joining(""));

        Instant agora = Instant.now();
        Instant expiracao = agora.plusSeconds(3600); // Token válido por 1 hora

        // 4. Constrói as permissões internas digitais do Token JWT
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("loja-api")
                .issuedAt(agora)
                .expiresAt(expiracao)
                .subject(usuario.getEmail())
                .claim("scope", escopos) // Armazena as Roles dinâmicas do banco aqui
                .build();

        JwtEncoderParameters parametros = JwtEncoderParameters.from(
                                                            JwsHeader.with(MacAlgorithm.HS256)
                                                                    .build(), claims);
        String tokenGerado = this.jwtEncoder.encode(parametros).getTokenValue();

        // Retorna o token em formato JSON de resposta para o cliente
        return ResponseEntity.ok(Map.of("token", tokenGerado));
    }

}
