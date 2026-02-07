package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.PrioridadDTO;

import java.util.List;

public interface PrioridadService {
    List<PrioridadDTO> findAll();

    PrioridadDTO findById(Integer id);

    PrioridadDTO create(PrioridadDTO dto);

    PrioridadDTO update(Integer id, PrioridadDTO dto);

    void delete(Integer id);
}

