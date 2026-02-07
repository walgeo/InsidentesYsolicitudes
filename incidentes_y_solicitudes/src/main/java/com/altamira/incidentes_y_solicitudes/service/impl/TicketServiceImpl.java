package com.altamira.incidentes_y_solicitudes.service.impl;

import com.altamira.incidentes_y_solicitudes.persistence.dto.TicketDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.TicketMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.EstadoRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.PrioridadRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.TicketRepository;
import com.altamira.incidentes_y_solicitudes.persistence.spec.TicketSpecifications;
import com.altamira.incidentes_y_solicitudes.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private static final String ESTADO_RESUELTO = "RESUELTO";
    private static final String ESTADO_CERRADO = "CERRADO";

    private final TicketRepository ticketRepository;
    private final PrioridadRepository prioridadRepository;
    private final EstadoRepository estadoRepository;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDTO> findAll(String aplicacion,
                                   Integer estadoId,
                                   Integer prioridadId,
                                   LocalDateTime creadoDesde,
                                   LocalDateTime creadoHasta,
                                   Pageable pageable) {
        log.info("🔍 Buscando tickets - aplicacion: {}, estadoId: {}, prioridadId: {}, pageable: {}",
                aplicacion, estadoId, prioridadId, pageable);
        try {
            Specification<Ticket> spec = Specification
                    .where(TicketSpecifications.aplicacionEquals(aplicacion))
                    .and(TicketSpecifications.estadoIdEquals(estadoId))
                    .and(TicketSpecifications.prioridadIdEquals(prioridadId))
                    .and(TicketSpecifications.creadoEnDesde(creadoDesde))
                    .and(TicketSpecifications.creadoEnHasta(creadoHasta));

            Page<Ticket> tickets = ticketRepository.findAll(spec, pageable);
            log.info("✅ Encontrados {} tickets", tickets.getTotalElements());

            Page<TicketDTO> result = tickets.map(ticketMapper::toDto);
            log.info("✅ Mapeados {} DTOs", result.getNumberOfElements());

            return result;
        } catch (Exception e) {
            log.error("❌ Error al buscar tickets", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDTO findById(Long id) {
        return ticketMapper.toDto(requireTicket(id));
    }

    @Override
    public TicketDTO create(TicketDTO dto) {
        validateTipo(dto.getTipo());
        Ticket ticket = ticketMapper.toEntity(dto);
        ticket.setPrioridad(requirePrioridad(dto.getPrioridadId()));
        ticket.setEstado(requireEstado(dto.getEstadoId()));
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Override
    public TicketDTO update(Long id, TicketDTO dto) {
        Ticket ticket = requireTicket(id);

        // Validar campos obligatorios
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Campo requerido: 'título' no puede estar vacío.");
        }
        if (dto.getAplicacion() == null || dto.getAplicacion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Campo requerido: 'aplicación' no puede estar vacía.");
        }
        if (dto.getTipo() == null || dto.getTipo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Campo requerido: 'tipo' no puede estar vacío. Debe ser 'incidente' o 'solicitud'.");
        }
        if (dto.getPrioridadId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Campo requerido: 'prioridad' debe ser seleccionada.");
        }
        if (dto.getEstadoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Campo requerido: 'estado' debe ser seleccionado.");
        }

        validateTipo(dto.getTipo());
        ticket.setTitulo(dto.getTitulo());
        ticket.setDescripcion(dto.getDescripcion());
        ticket.setTipo(dto.getTipo());
        ticket.setAplicacion(dto.getAplicacion());
        ticket.setAsignadoA(dto.getAsignadoA());
        if (dto.getPrioridadId() != null) {
            ticket.setPrioridad(requirePrioridad(dto.getPrioridadId()));
        }
        if (dto.getEstadoId() != null) {
            Estado nuevoEstado = requireEstado(dto.getEstadoId());
            validarTransicion(ticket.getEstado(), nuevoEstado);
            ticket.setEstado(nuevoEstado);
        }
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Override
    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado");
        }
        ticketRepository.deleteById(id);
    }

    private Ticket requireTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado"));
    }

    private Prioridad requirePrioridad(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prioridadId requerido");
        }
        return prioridadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridad no encontrada"));
    }

    private Estado requireEstado(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "estadoId requerido");
        }
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado"));
    }

    private void validarTransicion(Estado actual, Estado nuevo) {
        if (actual == null || nuevo == null) {
            return;
        }

        String estadoActual = actual.getNombre().toUpperCase();
        String estadoNuevo = nuevo.getNombre().toUpperCase();

        // Si es el mismo estado, no permitir
        if (estadoActual.equals(estadoNuevo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El ticket ya está en estado '" + actual.getNombre() + "'. Selecciona un estado diferente.");
        }

        // Definir el orden secuencial de transiciones permitidas
        // ABIERTO → EN_PROGRESO → RESUELTO → CERRADO

        if ("ABIERTO".equals(estadoActual)) {
            if ("EN_PROGRESO".equals(estadoNuevo)) {
                return; // Transición válida
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "No se puede pasar de 'ABIERTO' a '" + estadoNuevo + "'. " +
                        "El ticket debe pasar por los siguientes estados en orden: " +
                        "ABIERTO → EN_PROGRESO → RESUELTO → CERRADO. " +
                        "El siguiente estado debe ser 'EN_PROGRESO'.");
            }
        }

        if ("EN_PROGRESO".equals(estadoActual)) {
            if ("RESUELTO".equals(estadoNuevo)) {
                return; // Transición válida
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "No se puede pasar de 'EN_PROGRESO' a '" + estadoNuevo + "'. " +
                        "El ticket debe pasar por los siguientes estados en orden: " +
                        "ABIERTO → EN_PROGRESO → RESUELTO → CERRADO. " +
                        "El siguiente estado debe ser 'RESUELTO'.");
            }
        }

        if ("RESUELTO".equals(estadoActual)) {
            if ("CERRADO".equals(estadoNuevo)) {
                return; // Transición válida
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "No se puede pasar de 'RESUELTO' a '" + estadoNuevo + "'. " +
                        "El ticket debe pasar por los siguientes estados en orden: " +
                        "ABIERTO → EN_PROGRESO → RESUELTO → CERRADO. " +
                        "El siguiente estado debe ser 'CERRADO'.");
            }
        }

        if ("CERRADO".equals(estadoActual)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede cambiar el estado de un ticket que ya está 'CERRADO'. " +
                    "Los tickets cerrados son inmutables.");
        }

        // Cualquier otra transición es inválida
        throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Transición de estado inválida de '" + estadoActual + "' a '" + estadoNuevo + "'. " +
                "Los tickets deben seguir el orden: ABIERTO → EN_PROGRESO → RESUELTO → CERRADO.");
    }

    private void validateTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipo requerido");
        }
        String normalized = tipo.trim().toLowerCase();
        if (!"incidente".equals(normalized) && !"solicitud".equals(normalized)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipo debe ser 'incidente' o 'solicitud'");
        }
    }
}
