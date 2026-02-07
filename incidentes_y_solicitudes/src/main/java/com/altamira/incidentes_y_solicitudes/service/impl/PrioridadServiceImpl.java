package com.altamira.incidentes_y_solicitudes.service.impl;

import com.altamira.incidentes_y_solicitudes.persistence.dto.PrioridadDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.PrioridadMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.PrioridadRepository;
import com.altamira.incidentes_y_solicitudes.service.PrioridadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrioridadServiceImpl implements PrioridadService {

    private final PrioridadRepository prioridadRepository;
    private final PrioridadMapper prioridadMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PrioridadDTO> findAll() {
        return prioridadMapper.toDtoList(prioridadRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PrioridadDTO findById(Integer id) {
        return prioridadMapper.toDto(requirePrioridad(id));
    }

    @Override
    public PrioridadDTO create(PrioridadDTO dto) {
        Prioridad prioridad = prioridadMapper.toEntity(dto);
        return prioridadMapper.toDto(prioridadRepository.save(prioridad));
    }

    @Override
    public PrioridadDTO update(Integer id, PrioridadDTO dto) {
        Prioridad prioridad = requirePrioridad(id);
        prioridad.setNombre(dto.getNombre());
        return prioridadMapper.toDto(prioridadRepository.save(prioridad));
    }

    @Override
    public void delete(Integer id) {
        if (!prioridadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridad no encontrada");
        }
        prioridadRepository.deleteById(id);
    }

    private Prioridad requirePrioridad(Integer id) {
        return prioridadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridad no encontrada"));
    }
}

