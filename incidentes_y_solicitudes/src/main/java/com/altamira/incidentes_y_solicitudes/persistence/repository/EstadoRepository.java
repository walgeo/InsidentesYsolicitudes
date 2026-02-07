package com.altamira.incidentes_y_solicitudes.persistence.repository;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}

