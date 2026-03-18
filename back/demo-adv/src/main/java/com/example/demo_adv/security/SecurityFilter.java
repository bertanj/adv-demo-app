package com.example.demo_adv.security;

import com.example.demo_adv.model.service.AdvogadoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityFilter {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // 1. Removemos o .formLogin() e .httpBasic() para a tela branca do Spring sumir
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // 2. Liberar rotas públicas, estáticos do Vue e Swagger
                    auth.requestMatchers("/", "/index.html", "/static/**", "/css/**", "/js/**", "/favicon.ico").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/advogados").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();

                    // 3. Temporariamente permitimos tudo para você validar o visual do Dashboard
                    // No futuro, voltaremos para .authenticated() após configurar o JWT/Token
                    auth.anyRequest().permitAll();
                })
                .cors(Customizer.withDefaults()) // Ativa a permissão para o Vue (CORS)
                .build();
    }

    // Configuração essencial para o Vue (8081/3000) conseguir conversar com o Java (8080)
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Permite qualquer porta do localhost (8081, 8083, etc.)
        config.addAllowedOriginPattern("http://localhost:*");
        config.addAllowedOriginPattern("http://127.0.0.1:*");
        config.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(AdvogadoService advogadoService) {
        return new CustomUserDetails(advogadoService);
    }
}