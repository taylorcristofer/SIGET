package com.sgt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Mínimo 256 bits para HS256. Em produção, mova para application.properties
    private static final String SECRET = "siget-chave-secreta-super-segura-2024-jwt";
    private static final long EXPIRATION_MS = 86_400_000L; // 24 horas

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /** Gera token JWT para o usuário autenticado */
    public String gerarToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extrai o e-mail (subject) do token */
    public String extrairEmail(String token) {
        return parsearClaims(token).getSubject();
    }

    /** Extrai a role do token */
    public String extrairRole(String token) {
        return parsearClaims(token).get("role", String.class);
    }

    /** Valida o token: assinatura + expiração */
    public boolean validarToken(String token) {
        try {
            parsearClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parsearClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}