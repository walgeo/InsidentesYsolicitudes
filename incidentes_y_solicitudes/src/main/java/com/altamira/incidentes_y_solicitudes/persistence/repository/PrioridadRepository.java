package com.altamira.incidentes_y_solicitudes.persistence.repository;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Integer> {
}

