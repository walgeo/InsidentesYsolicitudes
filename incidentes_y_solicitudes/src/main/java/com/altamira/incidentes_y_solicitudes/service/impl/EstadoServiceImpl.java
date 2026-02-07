package com.altamira.incidentes_y_solicitudes.service.impl;

import com.altamira.incidentes_y_solicitudes.persistence.dto.EstadoDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.EstadoMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.EstadoRepository;
import com.altamira.incidentes_y_solicitudes.service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EstadoDTO> findAll() {
        return estadoMapper.toDtoList(estadoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoDTO findById(Integer id) {
        return estadoMapper.toDto(requireEstado(id));
    }

    @Override
    public EstadoDTO create(EstadoDTO dto) {
        Estado estado = estadoMapper.toEntity(dto);
        return estadoMapper.toDto(estadoRepository.save(estado));
    }

    @Override
    public EstadoDTO update(Integer id, EstadoDTO dto) {
        Estado estado = requireEstado(id);
        estado.setNombre(dto.getNombre());
        return estadoMapper.toDto(estadoRepository.save(estado));
    }

    @Override
    public void delete(Integer id) {
        if (!estadoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado");
        }
        estadoRepository.deleteById(id);
    }

    private Estado requireEstado(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado"));
    }
}

