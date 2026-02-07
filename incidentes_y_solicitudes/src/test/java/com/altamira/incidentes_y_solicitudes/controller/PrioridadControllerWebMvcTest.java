package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.PrioridadDTO;
import com.altamira.incidentes_y_solicitudes.service.PrioridadService;
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

@WebMvcTest(controllers = PrioridadController.class)
@AutoConfigureMockMvc(addFilters = false)
class PrioridadControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrioridadService prioridadService;

    @Test
    void testGetAll() throws Exception {
        when(prioridadService.findAll()).thenReturn(Collections.singletonList(new PrioridadDTO()));

        mockMvc.perform(get("/api/prioridades"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        PrioridadDTO dto = new PrioridadDTO();
        dto.setId(1);
        dto.setNombre("ALTA");
        when(prioridadService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/prioridades/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        PrioridadDTO dto = new PrioridadDTO();
        dto.setId(10);
        dto.setNombre("TEST");
        when(prioridadService.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/prioridades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"TEST\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        PrioridadDTO dto = new PrioridadDTO();
        dto.setId(1);
        dto.setNombre("UPDATED");
        when(prioridadService.update(eq(1), any())).thenReturn(dto);

        mockMvc.perform(put("/api/prioridades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"UPDATED\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/prioridades/1"))
                .andExpect(status().isNoContent());
        verify(prioridadService).delete(1);
    }
}

