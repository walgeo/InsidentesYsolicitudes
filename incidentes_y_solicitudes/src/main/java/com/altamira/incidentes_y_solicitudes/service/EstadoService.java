package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.EstadoDTO;

import java.util.List;

public interface EstadoService {
    List<EstadoDTO> findAll();

    EstadoDTO findById(Integer id);

    EstadoDTO create(EstadoDTO dto);

    EstadoDTO update(Integer id, EstadoDTO dto);

    void delete(Integer id);
}

