package com.altamira.incidentes_y_solicitudes.persistence.spec;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class TicketSpecifications {

    private TicketSpecifications() {
    }

    public static Specification<Ticket> aplicacionEquals(String aplicacion) {
        return (root, query, cb) -> aplicacion == null || aplicacion.isBlank()
                ? null
                : cb.equal(root.get("aplicacion"), aplicacion);
    }

    public static Specification<Ticket> estadoIdEquals(Integer estadoId) {
        return (root, query, cb) -> estadoId == null
                ? null
                : cb.equal(root.join("estado").get("id"), estadoId);
    }

    public static Specification<Ticket> prioridadIdEquals(Integer prioridadId) {
        return (root, query, cb) -> prioridadId == null
                ? null
                : cb.equal(root.join("prioridad").get("id"), prioridadId);
    }

    public static Specification<Ticket> creadoEnDesde(LocalDateTime desde) {
        return (root, query, cb) -> desde == null
                ? null
                : cb.greaterThanOrEqualTo(root.get("creadoEn"), desde);
    }

    public static Specification<Ticket> creadoEnHasta(LocalDateTime hasta) {
        return (root, query, cb) -> hasta == null
                ? null
                : cb.lessThanOrEqualTo(root.get("creadoEn"), hasta);
    }
}

