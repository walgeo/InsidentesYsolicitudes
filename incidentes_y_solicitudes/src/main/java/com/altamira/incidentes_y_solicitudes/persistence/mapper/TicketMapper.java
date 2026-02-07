package com.altamira.incidentes_y_solicitudes.persistence.mapper;

import com.altamira.incidentes_y_solicitudes.persistence.dto.TicketDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "prioridad.id", target = "prioridadId")
    @Mapping(source = "prioridad.nombre", target = "prioridadNombre")
    @Mapping(source = "estado.id", target = "estadoId")
    @Mapping(source = "estado.nombre", target = "estadoNombre")
    TicketDTO toDto(Ticket ticket);

    List<TicketDTO> toDtoList(List<Ticket> tickets);

    @Mapping(target = "prioridad", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Ticket toEntity(TicketDTO dto);
}

