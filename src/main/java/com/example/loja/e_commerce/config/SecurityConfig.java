package com.example.loja.e_commerce.config;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Captura a chave secreta que configuramos no application.properties
    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key-value}")
    private String jwtSecret;

    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/produtos").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
     */

    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF pois a API é stateless (baseada em tokens e não sessões de navegador)
                .csrf(csrf -> csrf.disable())

                // 2. Define as regras de autorização de requisições HTTP
                .authorizeHttpRequests(auth -> auth
                        // LIBERA explicitamente todas as rotas de autenticação (Login) de forma pública
                        .requestMatchers("/api/auth/**").permitAll()
                        // LIBERA a listagem de produtos pública
                        .requestMatchers(HttpMethod.GET, "/api/produtos").permitAll()
                        // LIBERA o console do banco H2 local (se aplicável)
                        .requestMatchers("/h2-console/**").permitAll()
                        // EXIGE autenticação estrita para qualquer outra rota (como salvar produtos)
                        .anyRequest().authenticated()
                )

                // 3. Configura o Resource Server para processar e aceitar Tokens JWT nas rotas protegidas
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        // Mantém a liberação de frames para ferramentas visuais de banco locais
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
    */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // LIBERA explicitamente o endpoint de login público
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/produtos").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    // Código adicionado para resolver o erro: Cria explicitamente o Decodificador do JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        // Converte a nossa String secreta em uma chave criptográfica real usando o algoritmo HMAC (SHA-256)
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecret.getBytes()));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
