package com.sgt.auth;

import com.sgt.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(), request.senha()
                )
            );

            UserDetails user = (UserDetails) auth.getPrincipal();
            String perfil = user.getAuthorities().iterator().next()
                              .getAuthority().replace("ROLE_", "");
            String token = jwtUtil.gerarToken(user.getUsername(), perfil);

            return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getUsername(),
                "role", perfil
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                .body(Map.of("erro", "E-mail ou senha inválidos"));
        }
    }

    @GetMapping("/hash")
    public String gerarHash(@RequestParam String senha) {
        return new BCryptPasswordEncoder().encode(senha);
    }
    
}

record LoginRequest(String email, String senha) {}