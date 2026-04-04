package com.example.demo_adv.controller;

import com.example.demo_adv.model.UserRole;
import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.service.AdvogadoService;
import com.example.demo_adv.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AdvogadoService advogadoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username e senha são obrigatórios"));
        }

        Advogado advogado = advogadoService.buscarPorEmail(username);

        if (advogado == null || !passwordEncoder.matches(password, advogado.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }

        String token = tokenService.generateToken(advogado);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "user", Map.of(
                "id", advogado.getId().toString(),
                "name", advogado.getName(),
                "email", advogado.getEmail(),
                "oab", advogado.getOab() != null ? advogado.getOab() : "",
                "role", advogado.getRole() != null ? advogado.getRole().name() : UserRole.ADVOGADO.name()
            )
        ));
    }
}
