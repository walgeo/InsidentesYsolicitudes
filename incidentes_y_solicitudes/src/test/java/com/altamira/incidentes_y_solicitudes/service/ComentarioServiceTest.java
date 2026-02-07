package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.ComentarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Comentario;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.ComentarioMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.ComentarioRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.TicketRepository;
import com.altamira.incidentes_y_solicitudes.service.impl.ComentarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ComentarioMapper comentarioMapper;

    @InjectMocks
    private ComentarioServiceImpl comentarioService;

    private ComentarioDTO comentarioDTO;
    private Comentario comentario;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1L);

        comentario = new Comentario();
        comentario.setId(1L);
        comentario.setMensaje("Test comment");
        comentario.setTicket(ticket);

        comentarioDTO = new ComentarioDTO();
        comentarioDTO.setId(1L);
        comentarioDTO.setMensaje("Test comment");
        comentarioDTO.setTicketId(1L);
    }

    @Test
    void testFindByTicketId_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(comentarioRepository.findByTicketIdOrderByCreadoEnAsc(1L))
                .thenReturn(Collections.singletonList(comentario));
        when(comentarioMapper.toDtoList(any())).thenReturn(Collections.singletonList(comentarioDTO));

        var result = comentarioService.findByTicketId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(comentarioRepository, times(1)).findByTicketIdOrderByCreadoEnAsc(1L);
    }

    @Test
    void testFindByTicketId_TicketNotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> comentarioService.findByTicketId(999L));
    }

    @Test
    void testCreate_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(comentarioMapper.toEntity(any())).thenReturn(comentario);
        when(comentarioRepository.save(any())).thenReturn(comentario);
        when(comentarioMapper.toDto(comentario)).thenReturn(comentarioDTO);

        var result = comentarioService.create(comentarioDTO);

        assertNotNull(result);
        assertEquals("Test comment", result.getMensaje());
        verify(comentarioRepository, times(1)).save(any());
    }

    @Test
    void testDelete_Success() {
        when(comentarioRepository.existsById(1L)).thenReturn(true);

        comentarioService.delete(1L);

        verify(comentarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(comentarioRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> comentarioService.delete(999L));
    }
}

