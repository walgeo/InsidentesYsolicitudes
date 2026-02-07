package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.EstadoDTO;
import com.altamira.incidentes_y_solicitudes.service.EstadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EstadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstadoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoService estadoService;

    @Test
    void testGetAll() throws Exception {
        when(estadoService.findAll()).thenReturn(Collections.singletonList(new EstadoDTO()));

        mockMvc.perform(get("/api/estados"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(1);
        dto.setNombre("ABIERTO");
        when(estadoService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/estados/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(10);
        dto.setNombre("TEST");
        when(estadoService.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"TEST\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(1);
        dto.setNombre("UPDATED");
        when(estadoService.update(eq(1), any())).thenReturn(dto);

        mockMvc.perform(put("/api/estados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"UPDATED\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/estados/1"))
                .andExpect(status().isNoContent());
        verify(estadoService).delete(1);
    }
}

