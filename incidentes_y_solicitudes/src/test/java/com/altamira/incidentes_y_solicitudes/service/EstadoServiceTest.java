package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.persistence.dto.EstadoDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.EstadoMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.EstadoRepository;
import com.altamira.incidentes_y_solicitudes.service.impl.EstadoServiceImpl;
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
class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private EstadoMapper estadoMapper;

    @InjectMocks
    private EstadoServiceImpl estadoService;

    private Estado estado;
    private EstadoDTO estadoDTO;

    @BeforeEach
    void setUp() {
        estado = new Estado();
        estado.setId(1);
        estado.setNombre("ABIERTO");

        estadoDTO = new EstadoDTO();
        estadoDTO.setId(1);
        estadoDTO.setNombre("ABIERTO");
    }

    @Test
    void testFindAll() {
        when(estadoRepository.findAll()).thenReturn(Collections.singletonList(estado));
        when(estadoMapper.toDtoList(any())).thenReturn(Collections.singletonList(estadoDTO));

        var result = estadoService.findAll();

        assertEquals(1, result.size());
        verify(estadoRepository).findAll();
    }

    @Test
    void testFindById() {
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));
        when(estadoMapper.toDto(estado)).thenReturn(estadoDTO);

        var result = estadoService.findById(1);

        assertEquals("ABIERTO", result.getNombre());
    }

    @Test
    void testFindById_NotFound() {
        when(estadoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> estadoService.findById(999));
    }

    @Test
    void testCreate() {
        when(estadoMapper.toEntity(any())).thenReturn(estado);
        when(estadoRepository.save(any())).thenReturn(estado);
        when(estadoMapper.toDto(estado)).thenReturn(estadoDTO);

        var result = estadoService.create(estadoDTO);

        assertEquals(1, result.getId());
        verify(estadoRepository).save(any());
    }

    @Test
    void testUpdate() {
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));
        when(estadoRepository.save(any())).thenReturn(estado);
        when(estadoMapper.toDto(estado)).thenReturn(estadoDTO);

        var result = estadoService.update(1, estadoDTO);

        assertEquals("ABIERTO", result.getNombre());
    }

    @Test
    void testDelete() {
        when(estadoRepository.existsById(1)).thenReturn(true);

        estadoService.delete(1);

        verify(estadoRepository).deleteById(1);
    }

    @Test
    void testDelete_NotFound() {
        when(estadoRepository.existsById(999)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> estadoService.delete(999));
    }
}

