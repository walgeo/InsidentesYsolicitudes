package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.ComentarioDTO;

import java.util.List;

public interface ComentarioService {
    List<ComentarioDTO> findAll();

    List<ComentarioDTO> findByTicketId(Long ticketId);

    ComentarioDTO findById(Long id);

    ComentarioDTO create(ComentarioDTO dto);

    ComentarioDTO update(Long id, ComentarioDTO dto);

    void delete(Long id);
}
