package com.example.demo_adv.security;

import com.example.demo_adv.model.entity.Advogado; // Ou sua entidade de Usuario
import com.example.demo_adv.model.service.AdvogadoService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetails implements UserDetailsService {

    private AdvogadoService advogadoService;

    public CustomUserDetails(AdvogadoService advogadoService) {
        this.advogadoService = advogadoService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca no banco usando a sua Service
        Advogado advogado = advogadoService.buscarPorEmail(email);

        if (advogado == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return new User(
                advogado.getEmail(),
                advogado.getPassword(),
                advogado.getRole().getAuthorities()
        );
    }
}