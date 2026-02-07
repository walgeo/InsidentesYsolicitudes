package com.altamira.incidentes_y_solicitudes.persistence.repository;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByTicketIdOrderByCreadoEnAsc(Long ticketId);
}
