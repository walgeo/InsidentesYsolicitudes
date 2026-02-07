package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.TicketDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TicketService {
    Page<TicketDTO> findAll(String aplicacion,
                            Integer estadoId,
                            Integer prioridadId,
                            LocalDateTime creadoDesde,
                            LocalDateTime creadoHasta,
                            Pageable pageable);

    TicketDTO findById(Long id);

    TicketDTO create(TicketDTO dto);

    TicketDTO update(Long id, TicketDTO dto);

    void delete(Long id);
}
