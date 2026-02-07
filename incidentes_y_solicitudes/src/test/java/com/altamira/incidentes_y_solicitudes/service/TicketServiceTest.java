package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.TicketDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.TicketMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.EstadoRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.PrioridadRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.TicketRepository;
import com.altamira.incidentes_y_solicitudes.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PrioridadRepository prioridadRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketDTO ticketDTO;
    private Ticket ticket;
    private Prioridad prioridad;
    private Estado estado;

    @BeforeEach
    void setUp() {
        prioridad = new Prioridad();
        prioridad.setId(1);
        prioridad.setNombre("ALTA");

        estado = new Estado();
        estado.setId(1);
        estado.setNombre("ABIERTO");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitulo("Test Ticket");
        ticket.setDescripcion("Test Description");
        ticket.setTipo("INCIDENTE");
        ticket.setAplicacion("APP1");
        ticket.setPrioridad(prioridad);
        ticket.setEstado(estado);

        ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);
        ticketDTO.setTitulo("Test Ticket");
        ticketDTO.setDescripcion("Test Description");
        ticketDTO.setTipo("INCIDENTE");
        ticketDTO.setAplicacion("APP1");
        ticketDTO.setPrioridadId(1);
        ticketDTO.setEstadoId(1);
    }

    @Test
    void testFindById_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        TicketDTO result = ticketService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitulo());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ticketService.findById(999L));
    }

    @Test
    void testCreate_Success() {
        when(prioridadRepository.findById(1)).thenReturn(Optional.of(prioridad));
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));
        when(ticketMapper.toEntity(any())).thenReturn(ticket);
        when(ticketRepository.save(any())).thenReturn(ticket);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        TicketDTO result = ticketService.create(ticketDTO);

        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitulo());
        verify(ticketRepository, times(1)).save(any());
    }

    @Test
    void testCreate_InvalidPrioridad() {
        when(prioridadRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ticketService.create(ticketDTO));
    }

    @Test
    void testUpdate_Success() {
        Estado nuevoEstado = new Estado();
        nuevoEstado.setId(2);
        nuevoEstado.setNombre("EN_PROGRESO");

        ticketDTO.setEstadoId(2);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(estadoRepository.findById(2)).thenReturn(Optional.of(nuevoEstado));
        when(prioridadRepository.findById(anyInt())).thenReturn(Optional.of(prioridad));
        when(ticketRepository.save(any())).thenReturn(ticket);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        TicketDTO result = ticketService.update(1L, ticketDTO);

        assertNotNull(result);
        verify(ticketRepository, times(1)).save(any());
    }

    @Test
    void testUpdate_InvalidTransition() {
        Estado resuelto = new Estado();
        resuelto.setId(3);
        resuelto.setNombre("RESUELTO");

        Estado cerrado = new Estado();
        cerrado.setId(4);
        cerrado.setNombre("CERRADO");

        ticket.setEstado(resuelto);
        ticketDTO.setEstadoId(4);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(estadoRepository.findById(4)).thenReturn(Optional.of(cerrado));
        when(prioridadRepository.findById(anyInt())).thenReturn(Optional.of(prioridad));
        when(ticketRepository.save(any())).thenReturn(ticket);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketDTO);

        TicketDTO result = ticketService.update(1L, ticketDTO);
        assertNotNull(result);
    }

    @Test
    void testDelete_Success() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        ticketService.delete(1L);

        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(ticketRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> ticketService.delete(999L));
    }
}

