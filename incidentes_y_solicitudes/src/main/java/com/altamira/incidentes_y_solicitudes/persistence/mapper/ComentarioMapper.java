package com.altamira.incidentes_y_solicitudes.persistence.mapper;

import com.altamira.incidentes_y_solicitudes.persistence.dto.ComentarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Comentario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    @Mapping(source = "ticket.id", target = "ticketId")
    ComentarioDTO toDto(Comentario comentario);

    List<ComentarioDTO> toDtoList(List<Comentario> comentarios);

    @Mapping(target = "ticket", ignore = true)
    Comentario toEntity(ComentarioDTO dto);
}

