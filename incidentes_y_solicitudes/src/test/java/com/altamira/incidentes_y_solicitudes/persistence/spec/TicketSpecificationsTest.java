package com.altamira.incidentes_y_solicitudes.persistence.spec;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketSpecificationsTest {

    @Mock
    private Root<Ticket> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Predicate predicate;

    @Mock
    private Path<Object> stringPath;

    @Mock
    private Path<Object> intPath;

    @Mock
    private Path<Object> datePath;

    @Mock
    private Join<Object, Object> join;

    @Test
    void aplicacionEquals_NullOrBlank_ReturnsNullPredicate() {
        assertNull(TicketSpecifications.aplicacionEquals(null).toPredicate(root, query, cb));
        assertNull(TicketSpecifications.aplicacionEquals(" ").toPredicate(root, query, cb));
        verifyNoInteractions(cb);
    }

    @Test
    void aplicacionEquals_Value_ReturnsPredicate() {
        when(root.get("aplicacion")).thenReturn(stringPath);
        when(cb.equal(stringPath, "APP")).thenReturn(predicate);

        Predicate result = TicketSpecifications.aplicacionEquals("APP").toPredicate(root, query, cb);

        assertNotNull(result);
        verify(cb).equal(stringPath, "APP");
    }

    @Test
    void estadoIdEquals_Null_ReturnsNullPredicate() {
        assertNull(TicketSpecifications.estadoIdEquals(null).toPredicate(root, query, cb));
        verifyNoInteractions(cb);
    }

    @Test
    void estadoIdEquals_Value_ReturnsPredicate() {
        when(root.join("estado")).thenReturn(join);
        when(join.get("id")).thenReturn(intPath);
        when(cb.equal(intPath, 1)).thenReturn(predicate);

        Predicate result = TicketSpecifications.estadoIdEquals(1).toPredicate(root, query, cb);

        assertNotNull(result);
        verify(cb).equal(intPath, 1);
    }

    @Test
    void prioridadIdEquals_Null_ReturnsNullPredicate() {
        assertNull(TicketSpecifications.prioridadIdEquals(null).toPredicate(root, query, cb));
        verifyNoInteractions(cb);
    }

    @Test
    void prioridadIdEquals_Value_ReturnsPredicate() {
        when(root.join("prioridad")).thenReturn(join);
        when(join.get("id")).thenReturn(intPath);
        when(cb.equal(intPath, 2)).thenReturn(predicate);

        Predicate result = TicketSpecifications.prioridadIdEquals(2).toPredicate(root, query, cb);

        assertNotNull(result);
        verify(cb).equal(intPath, 2);
    }

    @Test
    void creadoEnDesde_Null_ReturnsNullPredicate() {
        assertNull(TicketSpecifications.creadoEnDesde(null).toPredicate(root, query, cb));
        verifyNoInteractions(cb);
    }

    @Test
    @SuppressWarnings("unchecked")
    void creadoEnDesde_Value_ReturnsPredicate() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        when(root.get("creadoEn")).thenReturn((Path) datePath);
        when(cb.greaterThanOrEqualTo((Path) datePath, from)).thenReturn(predicate);

        Predicate result = TicketSpecifications.creadoEnDesde(from).toPredicate(root, query, cb);

        assertNotNull(result);
        verify(cb).greaterThanOrEqualTo((Path) datePath, from);
    }

    @Test
    void creadoEnHasta_Null_ReturnsNullPredicate() {
        assertNull(TicketSpecifications.creadoEnHasta(null).toPredicate(root, query, cb));
        verifyNoInteractions(cb);
    }

    @Test
    @SuppressWarnings("unchecked")
    void creadoEnHasta_Value_ReturnsPredicate() {
        LocalDateTime to = LocalDateTime.now();
        when(root.get("creadoEn")).thenReturn((Path) datePath);
        when(cb.lessThanOrEqualTo((Path) datePath, to)).thenReturn(predicate);

        Predicate result = TicketSpecifications.creadoEnHasta(to).toPredicate(root, query, cb);

        assertNotNull(result);
        verify(cb).lessThanOrEqualTo((Path) datePath, to);
    }
}

