package com.altamira.incidentes_y_solicitudes.persistence.mapper;

import com.altamira.incidentes_y_solicitudes.persistence.dto.PrioridadDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrioridadMapper {
    PrioridadDTO toDto(Prioridad prioridad);

    List<PrioridadDTO> toDtoList(List<Prioridad> prioridades);

    Prioridad toEntity(PrioridadDTO dto);
}

