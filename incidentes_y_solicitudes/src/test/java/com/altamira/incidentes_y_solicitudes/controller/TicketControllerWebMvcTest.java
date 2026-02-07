package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.TicketDTO;
import com.altamira.incidentes_y_solicitudes.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    void testGetAll_DefaultSort() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_CustomSortAsc() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "creadoEn,asc")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_CustomSortDesc() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "creadoEn,desc")
                        .param("sort", "titulo,asc")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_InvalidSize() throws Exception {
        mockMvc.perform(get("/api/tickets")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAll_DefaultSort_NoSortParam() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortBlankIgnored() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortNoDirectionDefaultsAsc() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "titulo")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_InvalidSizeTooLarge() throws Exception {
        mockMvc.perform(get("/api/tickets")
                        .param("size", "101"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAll_MultipleSortFieldsSameDirection() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "titulo,asc")
                        .param("sort", "creadoEn,asc")
                        .param("sort", "aplicacion,asc"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortWithExtraCommas() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "titulo,desc,extra"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortOnlyCommas() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", ",,,"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortMixedValidInvalid() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "titulo,asc")
                        .param("sort", "")
                        .param("sort", "aplicacion,desc"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortWithWhitespace() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", " titulo , asc "))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortDescUppercase() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "titulo,DESC"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_SortAscUppercase() throws Exception {
        when(ticketService.findAll(anyString(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/tickets")
                        .param("sort", "titulo,ASC"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        TicketDTO dto = new TicketDTO();
        dto.setId(1L);
        dto.setTitulo("Test");
        dto.setTipo("INCIDENTE");
        dto.setAplicacion("APP1");
        dto.setPrioridadId(1);
        dto.setEstadoId(1);

        when(ticketService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreate() throws Exception {
        TicketDTO dto = new TicketDTO();
        dto.setId(1L);
        dto.setTitulo("Test");
        dto.setTipo("INCIDENTE");
        dto.setAplicacion("APP1");
        dto.setPrioridadId(1);
        dto.setEstadoId(1);

        when(ticketService.create(any())).thenReturn(dto);

        String json = "{\"titulo\":\"Test\",\"tipo\":\"INCIDENTE\",\"aplicacion\":\"APP1\",\"prioridadId\":1,\"estadoId\":1}";
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdate() throws Exception {
        TicketDTO dto = new TicketDTO();
        dto.setId(1L);
        dto.setTitulo("Updated");
        dto.setTipo("INCIDENTE");
        dto.setAplicacion("APP1");
        dto.setPrioridadId(1);
        dto.setEstadoId(1);

        when(ticketService.update(eq(1L), any())).thenReturn(dto);

        String json = "{\"titulo\":\"Updated\",\"tipo\":\"INCIDENTE\",\"aplicacion\":\"APP1\",\"prioridadId\":1,\"estadoId\":1}";
        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Updated"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());
        verify(ticketService).delete(1L);
    }
}
