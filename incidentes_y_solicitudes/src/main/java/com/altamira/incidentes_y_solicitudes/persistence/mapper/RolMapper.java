package com.altamira.incidentes_y_solicitudes.persistence.mapper;

import com.altamira.incidentes_y_solicitudes.controller.dto.RolDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {
    RolDTO toDto(Rol entity);
    Rol toEntity(RolDTO dto);
}

