package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.PrioridadDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.PrioridadMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.PrioridadRepository;
import com.altamira.incidentes_y_solicitudes.service.impl.PrioridadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrioridadServiceTest {

    @Mock
    private PrioridadRepository prioridadRepository;

    @Mock
    private PrioridadMapper prioridadMapper;

    @InjectMocks
    private PrioridadServiceImpl prioridadService;

    private Prioridad prioridad;
    private PrioridadDTO prioridadDTO;

    @BeforeEach
    void setUp() {
        prioridad = new Prioridad();
        prioridad.setId(1);
        prioridad.setNombre("ALTA");

        prioridadDTO = new PrioridadDTO();
        prioridadDTO.setId(1);
        prioridadDTO.setNombre("ALTA");
    }

    @Test
    void testFindAll() {
        when(prioridadRepository.findAll()).thenReturn(Collections.singletonList(prioridad));
        when(prioridadMapper.toDtoList(any())).thenReturn(Collections.singletonList(prioridadDTO));

        var result = prioridadService.findAll();

        assertEquals(1, result.size());
        verify(prioridadRepository).findAll();
    }

    @Test
    void testFindById() {
        when(prioridadRepository.findById(1)).thenReturn(Optional.of(prioridad));
        when(prioridadMapper.toDto(prioridad)).thenReturn(prioridadDTO);

        var result = prioridadService.findById(1);

        assertEquals("ALTA", result.getNombre());
    }

    @Test
    void testFindById_NotFound() {
        when(prioridadRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> prioridadService.findById(999));
    }

    @Test
    void testCreate() {
        when(prioridadMapper.toEntity(any())).thenReturn(prioridad);
        when(prioridadRepository.save(any())).thenReturn(prioridad);
        when(prioridadMapper.toDto(prioridad)).thenReturn(prioridadDTO);

        var result = prioridadService.create(prioridadDTO);

        assertEquals(1, result.getId());
        verify(prioridadRepository).save(any());
    }

    @Test
    void testUpdate() {
        when(prioridadRepository.findById(1)).thenReturn(Optional.of(prioridad));
        when(prioridadRepository.save(any())).thenReturn(prioridad);
        when(prioridadMapper.toDto(prioridad)).thenReturn(prioridadDTO);

        var result = prioridadService.update(1, prioridadDTO);

        assertEquals("ALTA", result.getNombre());
    }

    @Test
    void testDelete() {
        when(prioridadRepository.existsById(1)).thenReturn(true);

        prioridadService.delete(1);

        verify(prioridadRepository).deleteById(1);
    }

    @Test
    void testDelete_NotFound() {
        when(prioridadRepository.existsById(999)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> prioridadService.delete(999));
    }
}

