package com.example.demo_adv.security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo_adv.model.repositores.AdvogadoRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityTokenFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        logger.info("Token recuperado: {}", token != null ? "SIM" : "NÃO");
        
        if (token != null) {
            var email = tokenService.validateToken(token);
            logger.info("Email do token: {}", email);
            
            if (!email.isEmpty()) {
                // Extrai a role direto do token — sem query extra no banco só pra isso
                var role = tokenService.extractRole(token);
                logger.info("Role do token: {}", role);
                
                List<SimpleGrantedAuthority> authorities = role != null
                        ? role.getAuthorities()
                        : List.of();

                // Usa o email como principal — o banco só é consultado se precisar dos dados completos
                var authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Autenticação configurada para: {}", email);
            } else {
                logger.warn("Token inválido ou expirado!");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        logger.info("Authorization Header: {}", authHeader);
        if (authHeader == null) {
            logger.warn("Authorization header não encontrado!");
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
