package com.altamira.incidentes_y_solicitudes.persistence.spec;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import com.altamira.incidentes_y_solicitudes.persistence.repository.EstadoRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.PrioridadRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TicketSpecificationsIntegrationTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private PrioridadRepository prioridadRepository;

    private Estado estado1;
    private Prioridad prioridad1;
    private Prioridad prioridad2;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();

        estado1 = estadoRepository.findById(1).orElseThrow();
        prioridad1 = prioridadRepository.findById(1).orElseThrow();
        prioridad2 = prioridadRepository.findById(2).orElseThrow();

        // Crear tickets de prueba
        Ticket t1 = new Ticket();
        t1.setTitulo("Ticket 1");
        t1.setDescripcion("Desc 1");
        t1.setTipo("INCIDENTE");
        t1.setAplicacion("APP1");
        t1.setEstado(estado1);
        t1.setPrioridad(prioridad1);
        t1.setCreadoPor("USER1");
        ticketRepository.save(t1);

        Ticket t2 = new Ticket();
        t2.setTitulo("Ticket 2");
        t2.setDescripcion("Desc 2");
        t2.setTipo("SOLICITUD");
        t2.setAplicacion("APP2");
        t2.setEstado(estado1);
        t2.setPrioridad(prioridad2);
        t2.setCreadoPor("USER2");
        ticketRepository.save(t2);

        Ticket t3 = new Ticket();
        t3.setTitulo("Ticket 3");
        t3.setDescripcion("Desc 3");
        t3.setTipo("INCIDENTE");
        t3.setAplicacion("APP1");
        t3.setEstado(estado1);
        t3.setPrioridad(prioridad1);
        t3.setCreadoPor("USER1");
        ticketRepository.save(t3);
    }

    @Test
    void testAplicacionEquals_WithValue() {
        Specification<Ticket> spec = TicketSpecifications.aplicacionEquals("APP1");
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t -> "APP1".equals(t.getAplicacion())));
    }

    @Test
    void testAplicacionEquals_WithNull() {
        Specification<Ticket> spec = TicketSpecifications.aplicacionEquals(null);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size()); // Devuelve todos
    }

    @Test
    void testAplicacionEquals_WithBlank() {
        Specification<Ticket> spec = TicketSpecifications.aplicacionEquals("  ");
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size()); // Devuelve todos
    }

    @Test
    void testEstadoIdEquals_WithValue() {
        Specification<Ticket> spec = TicketSpecifications.estadoIdEquals(1);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(t -> t.getEstado().getId() == 1));
    }

    @Test
    void testEstadoIdEquals_WithNull() {
        Specification<Ticket> spec = TicketSpecifications.estadoIdEquals(null);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
    }

    @Test
    void testPrioridadIdEquals_WithValue() {
        Specification<Ticket> spec = TicketSpecifications.prioridadIdEquals(1);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t -> t.getPrioridad().getId() == 1));
    }

    @Test
    void testPrioridadIdEquals_WithNull() {
        Specification<Ticket> spec = TicketSpecifications.prioridadIdEquals(null);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
    }

    @Test
    void testCreadoEnDesde_WithValue() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Specification<Ticket> spec = TicketSpecifications.creadoEnDesde(yesterday);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(t -> t.getCreadoEn().isAfter(yesterday) || t.getCreadoEn().isEqual(yesterday)));
    }

    @Test
    void testCreadoEnDesde_WithNull() {
        Specification<Ticket> spec = TicketSpecifications.creadoEnDesde(null);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
    }

    @Test
    void testCreadoEnHasta_WithValue() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Specification<Ticket> spec = TicketSpecifications.creadoEnHasta(tomorrow);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(t -> t.getCreadoEn().isBefore(tomorrow) || t.getCreadoEn().isEqual(tomorrow)));
    }

    @Test
    void testCreadoEnHasta_WithNull() {
        Specification<Ticket> spec = TicketSpecifications.creadoEnHasta(null);
        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
    }

    @Test
    void testCombinedSpecifications() {
        Specification<Ticket> spec = Specification
                .where(TicketSpecifications.aplicacionEquals("APP1"))
                .and(TicketSpecifications.prioridadIdEquals(1))
                .and(TicketSpecifications.estadoIdEquals(1));

        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t ->
            "APP1".equals(t.getAplicacion()) &&
            t.getPrioridad().getId() == 1 &&
            t.getEstado().getId() == 1
        ));
    }

    @Test
    void testCombinedSpecifications_WithDateRange() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        Specification<Ticket> spec = Specification
                .where(TicketSpecifications.creadoEnDesde(yesterday))
                .and(TicketSpecifications.creadoEnHasta(tomorrow));

        List<Ticket> results = ticketRepository.findAll(spec);

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(t ->
            (t.getCreadoEn().isAfter(yesterday) || t.getCreadoEn().isEqual(yesterday)) &&
            (t.getCreadoEn().isBefore(tomorrow) || t.getCreadoEn().isEqual(tomorrow))
        ));
    }
}

