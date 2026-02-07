package com.altamira.incidentes_y_solicitudes.persistence.repository;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByActivoTrue();
}

