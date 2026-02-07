package com.altamira.incidentes_y_solicitudes.persistence.mapper;

import com.altamira.incidentes_y_solicitudes.persistence.dto.EstadoDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EstadoMapper {
    EstadoDTO toDto(Estado estado);

    List<EstadoDTO> toDtoList(List<Estado> estados);

    Estado toEntity(EstadoDTO dto);
}

