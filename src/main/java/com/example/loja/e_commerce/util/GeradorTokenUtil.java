package com.example.loja.e_commerce.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class GeradorTokenUtil implements CommandLineRunner {

    private final JwtEncoder jwtEncoder;

    // Construtor recebendo o encoder que vamos configurar no próximo passo
    public GeradorTokenUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Instant agora = Instant.now();
        Instant expiracao = agora.plusSeconds(3600); // Token válido por 1 hora

        // Define os dados de dentro do Token (Claims)
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("loja-api")
                .issuedAt(agora)
                .expiresAt(expiracao)
                .subject("usuario_admin")
                .claim("scope", "ROLE_ADMIN") // Permissões
                .build();

        // Encodando o token com o algoritmo HMAC SHA-256 esperado pela configuração
        JwtEncoderParameters parametros = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        String tokenGerado = this.jwtEncoder.encode(parametros).getTokenValue();

        System.out.println("\n🔑 [TOKEN DE TESTE GERADO COM SUCESSO]");
        System.out.println("Bearer " + tokenGerado);
        System.out.println("=================================================================================\n");
    }
}
