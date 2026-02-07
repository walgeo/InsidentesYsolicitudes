package com.altamira.incidentes_y_solicitudes.persistence.mapper;

import com.altamira.incidentes_y_solicitudes.controller.dto.UsuarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "rolId", source = "rol.id")
    @Mapping(target = "rolNombre", source = "rol.nombre")
    UsuarioDTO toDto(Usuario entity);

    @Mapping(target = "rol", ignore = true)
    Usuario toEntity(UsuarioDTO dto);
}

