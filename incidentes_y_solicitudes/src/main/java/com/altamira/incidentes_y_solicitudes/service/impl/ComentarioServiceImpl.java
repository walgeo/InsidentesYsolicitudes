package com.altamira.incidentes_y_solicitudes.service.impl;

import com.altamira.incidentes_y_solicitudes.persistence.dto.ComentarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Comentario;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Ticket;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.ComentarioMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.ComentarioRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.TicketRepository;
import com.altamira.incidentes_y_solicitudes.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TicketRepository ticketRepository;
    private final ComentarioMapper comentarioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDTO> findAll() {
        return comentarioMapper.toDtoList(comentarioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDTO> findByTicketId(Long ticketId) {
        requireTicket(ticketId);
        return comentarioMapper.toDtoList(comentarioRepository.findByTicketIdOrderByCreadoEnAsc(ticketId));
    }

    @Override
    @Transactional(readOnly = true)
    public ComentarioDTO findById(Long id) {
        return comentarioMapper.toDto(requireComentario(id));
    }

    @Override
    public ComentarioDTO create(ComentarioDTO dto) {
        Comentario comentario = comentarioMapper.toEntity(dto);
        comentario.setTicket(requireTicket(dto.getTicketId()));
        return comentarioMapper.toDto(comentarioRepository.save(comentario));
    }

    @Override
    public ComentarioDTO update(Long id, ComentarioDTO dto) {
        Comentario comentario = requireComentario(id);
        comentario.setMensaje(dto.getMensaje());
        if (dto.getTicketId() != null) {
            comentario.setTicket(requireTicket(dto.getTicketId()));
        }
        return comentarioMapper.toDto(comentarioRepository.save(comentario));
    }

    @Override
    public void delete(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado");
        }
        comentarioRepository.deleteById(id);
    }

    private Comentario requireComentario(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));
    }

    private Ticket requireTicket(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ticketId requerido");
        }
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado"));
    }
}
