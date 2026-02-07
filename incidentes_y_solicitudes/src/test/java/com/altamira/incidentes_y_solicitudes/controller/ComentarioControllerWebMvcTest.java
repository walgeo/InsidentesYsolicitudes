package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.ComentarioDTO;
import com.altamira.incidentes_y_solicitudes.service.ComentarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ComentarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class ComentarioControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentarioService comentarioService;

    @Test
    void testGetAll() throws Exception {
        when(comentarioService.findAll()).thenReturn(Collections.singletonList(new ComentarioDTO()));

        mockMvc.perform(get("/api/comentarios"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(1L);
        dto.setMensaje("Test");
        when(comentarioService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/comentarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByTicket() throws Exception {
        when(comentarioService.findByTicketId(1L)).thenReturn(Collections.singletonList(new ComentarioDTO()));

        mockMvc.perform(get("/api/comentarios/ticket/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(1L);
        dto.setMensaje("Test");
        when(comentarioService.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ticketId\":1,\"mensaje\":\"Test\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(1L);
        dto.setMensaje("Updated");
        when(comentarioService.update(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/comentarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mensaje\":\"Updated\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/comentarios/1"))
                .andExpect(status().isNoContent());
        verify(comentarioService).delete(1L);
    }
}

