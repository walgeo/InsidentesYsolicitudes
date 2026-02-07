package com.altamira.incidentes_y_solicitudes.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tickets", schema = "incidentes_db")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_gen")
    @SequenceGenerator(name = "ticket_gen", sequenceName = "ticket_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, length = 50)
    private String aplicacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prioridad_id", nullable = false)
    private Prioridad prioridad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @Column(name = "asignado_a", length = 50)
    private String asignadoA;
}