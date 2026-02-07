package com.altamira.incidentes_y_solicitudes.persistence.mapper;

import com.altamira.incidentes_y_solicitudes.persistence.dto.ComentarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.dto.EstadoDTO;
import com.altamira.incidentes_y_solicitudes.persistence.dto.PrioridadDTO;
import com.altamira.incidentes_y_solicitudes.persistence.dto.TicketDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Comentario;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MapperTest {

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Autowired
    private EstadoMapper estadoMapper;

    @Autowired
    private PrioridadMapper prioridadMapper;

    @Autowired
    private TicketMapper ticketMapper;

    // ComentarioMapper Tests
    @Test
    void testComentarioMapper_ToDto() {
        Comentario comentario = new Comentario();
        comentario.setId(1L);
        comentario.setMensaje("Test mensaje");
        comentario.setCreadoPor("USER1");
        comentario.setCreadoEn(LocalDateTime.now());

        ComentarioDTO dto = comentarioMapper.toDto(comentario);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test mensaje", dto.getMensaje());
        assertEquals("USER1", dto.getCreadoPor());
    }

    @Test
    void testComentarioMapper_ToEntity() {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(1L);
        dto.setMensaje("Test mensaje");
        dto.setCreadoPor("USER1");

        Comentario entity = comentarioMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Test mensaje", entity.getMensaje());
        assertEquals("USER1", entity.getCreadoPor());
    }

    @Test
    void testComentarioMapper_ToDtoList() {
        Comentario c1 = new Comentario();
        c1.setId(1L);
        c1.setMensaje("Msg1");

        Comentario c2 = new Comentario();
        c2.setId(2L);
        c2.setMensaje("Msg2");

        List<ComentarioDTO> dtos = comentarioMapper.toDtoList(Arrays.asList(c1, c2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    // EstadoMapper Tests
    @Test
    void testEstadoMapper_ToDto() {
        Estado estado = new Estado();
        estado.setId(1);
        estado.setNombre("ABIERTO");
        estado.setCreadoPor("SYSTEM");

        EstadoDTO dto = estadoMapper.toDto(estado);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("ABIERTO", dto.getNombre());
    }

    @Test
    void testEstadoMapper_ToEntity() {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(1);
        dto.setNombre("ABIERTO");

        Estado entity = estadoMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1, entity.getId());
        assertEquals("ABIERTO", entity.getNombre());
    }

    @Test
    void testEstadoMapper_ToDtoList() {
        Estado e1 = new Estado();
        e1.setId(1);
        e1.setNombre("ABIERTO");

        Estado e2 = new Estado();
        e2.setId(2);
        e2.setNombre("CERRADO");

        List<EstadoDTO> dtos = estadoMapper.toDtoList(Arrays.asList(e1, e2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    // PrioridadMapper Tests
    @Test
    void testPrioridadMapper_ToDto() {
        Prioridad prioridad = new Prioridad();
        prioridad.setId(1);
        prioridad.setNombre("ALTA");
        prioridad.setCreadoPor("SYSTEM");

        PrioridadDTO dto = prioridadMapper.toDto(prioridad);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("ALTA", dto.getNombre());
    }

    @Test
    void testPrioridadMapper_ToEntity() {
        PrioridadDTO dto = new PrioridadDTO();
        dto.setId(1);
        dto.setNombre("ALTA");

        Prioridad entity = prioridadMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1, entity.getId());
        assertEquals("ALTA", entity.getNombre());
    }

    @Test
    void testPrioridadMapper_ToDtoList() {
        Prioridad p1 = new Prioridad();
        p1.setId(1);
        p1.setNombre("ALTA");

        Prioridad p2 = new Prioridad();
        p2.setId(2);
        p2.setNombre("BAJA");

        List<PrioridadDTO> dtos = prioridadMapper.toDtoList(Arrays.asList(p1, p2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    // TicketMapper Tests - additional coverage
    @Test
    void testTicketMapper_ToDto_WithAllFields() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitulo("Test Ticket");
        ticket.setDescripcion("Description");
        ticket.setTipo("INCIDENTE");
        ticket.setAplicacion("APP1");

        Estado estado = new Estado();
        estado.setId(1);
        estado.setNombre("ABIERTO");
        ticket.setEstado(estado);

        Prioridad prioridad = new Prioridad();
        prioridad.setId(1);
        prioridad.setNombre("ALTA");
        ticket.setPrioridad(prioridad);

        ticket.setCreadoPor("USER1");
        ticket.setAsignadoA("USER2");

        TicketDTO dto = ticketMapper.toDto(ticket);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Ticket", dto.getTitulo());
        assertEquals("ABIERTO", dto.getEstadoNombre());
        assertEquals("ALTA", dto.getPrioridadNombre());
    }

    @Test
    void testTicketMapper_ToEntity_WithAllFields() {
        TicketDTO dto = new TicketDTO();
        dto.setId(1L);
        dto.setTitulo("Test Ticket");
        dto.setDescripcion("Description");
        dto.setTipo("INCIDENTE");
        dto.setAplicacion("APP1");
        dto.setEstadoId(1);
        dto.setPrioridadId(1);
        dto.setCreadoPor("USER1");
        dto.setAsignadoA("USER2");

        Ticket entity = ticketMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Test Ticket", entity.getTitulo());
        assertEquals("INCIDENTE", entity.getTipo());
    }

    @Test
    void testTicketMapper_ToDtoList() {
        Ticket t1 = new Ticket();
        t1.setId(1L);
        t1.setTitulo("Ticket1");

        Ticket t2 = new Ticket();
        t2.setId(2L);
        t2.setTitulo("Ticket2");

        List<TicketDTO> dtos = ticketMapper.toDtoList(Arrays.asList(t1, t2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }
}

