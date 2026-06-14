package com.sgt.repository;

import com.sgt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
     Optional<Usuario> findByEmailAndAtivoTrue(String email);
}
