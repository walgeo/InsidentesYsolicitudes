package com.altamira.incidentes_y_solicitudes;

import com.altamira.incidentes_y_solicitudes.persistence.entity.Estado;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Prioridad;
import com.altamira.incidentes_y_solicitudes.persistence.repository.EstadoRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.PrioridadRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IncidentesYSolicitudesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private PrioridadRepository prioridadRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Solo limpiar tickets, estados y prioridades ya están precargados por Flyway
        ticketRepository.deleteAll();
    }

    @Test
    void testCreateAndRetrieveTicket_HappyPath() throws Exception {
        String ticketJson = """
                {
                  "titulo": "Integration Test Ticket",
                  "descripcion": "Testing create and retrieve",
                  "tipo": "INCIDENTE",
                  "aplicacion": "TEST_APP",
                  "prioridadId": 1,
                  "estadoId": 1,
                  "creadoPor": "TEST_USER"
                }
                """;

        var createResponse = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.titulo", equalTo("Integration Test Ticket")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long ticketId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/tickets/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(ticketId.intValue())))
                .andExpect(jsonPath("$.titulo", equalTo("Integration Test Ticket")));
    }

    @Test
    void testCreateTicketWithInvalidType_Error() throws Exception {
        String ticketJson = """
                {
                  "titulo": "Test Ticket",
                  "descripcion": "Test Description",
                  "tipo": "INVALID_TYPE",
                  "aplicacion": "APP1",
                  "prioridadId": 1,
                  "estadoId": 1,
                  "creadoPor": "USER1"
                }
                """;

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("tipo debe ser")));
    }

    @Test
    void testCreateTicketWithMissingFields_Error() throws Exception {
        String ticketJson = """
                {
                  "titulo": "Test Ticket"
                }
                """;

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validacion")))
                .andExpect(jsonPath("$.details", notNullValue()));
    }

    @Test
    void testGetNonExistentTicket_Error() throws Exception {
        mockMvc.perform(get("/api/tickets/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Ticket no encontrado")))
                .andExpect(jsonPath("$.status", equalTo(404)));
    }

    @Test
    void testListTicketsWithPagination() throws Exception {
        for (int i = 0; i < 5; i++) {
            String ticketJson = String.format("""
                    {
                      "titulo": "Ticket %d",
                      "descripcion": "Description %d",
                      "tipo": "INCIDENTE",
                      "aplicacion": "APP1",
                      "prioridadId": 1,
                      "estadoId": 1,
                      "creadoPor": "USER1"
                    }
                    """, i, i);

            mockMvc.perform(post("/api/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ticketJson))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/tickets?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements", equalTo(5)))
                .andExpect(jsonPath("$.totalPages", equalTo(2)));
    }

    @Test
    void testGetAllEstados() throws Exception {
        mockMvc.perform(get("/api/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetEstadoById() throws Exception {
        mockMvc.perform(get("/api/estados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetAllPrioridades() throws Exception {
        mockMvc.perform(get("/api/prioridades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetPrioridadById() throws Exception {
        mockMvc.perform(get("/api/prioridades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetAllComentarios() throws Exception {
        mockMvc.perform(get("/api/comentarios"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetComentariosByTicket() throws Exception {
        String ticketJson = """
                {
                  "titulo": "Ticket con comentario",
                  "descripcion": "Descripcion del ticket con comentario",
                  "tipo": "INCIDENTE",
                  "aplicacion": "APP1",
                  "prioridadId": 1,
                  "estadoId": 1,
                  "creadoPor": "USER1"
                }
                """;

        var ticketResponse = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long ticketId = objectMapper.readTree(ticketResponse).get("id").asLong();

        String comentarioJson = String.format("""
                {
                  "ticketId": %d,
                  "mensaje": "Comentario 1",
                  "creadoPor": "USER1"
                }
                """, ticketId);

        mockMvc.perform(post("/api/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comentarioJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/comentarios/ticket/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testFilterTicketsByAplicacion() throws Exception {
        String ticket1 = """
                {
                  "titulo": "Ticket APP1",
                  "tipo": "INCIDENTE",
                  "aplicacion": "APP1",
                  "prioridadId": 1,
                  "estadoId": 1,
                  "creadoPor": "USER1"
                }
                """;

        String ticket2 = """
                {
                  "titulo": "Ticket APP2",
                  "tipo": "INCIDENTE",
                  "aplicacion": "APP2",
                  "prioridadId": 1,
                  "estadoId": 1,
                  "creadoPor": "USER1"
                }
                """;

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticket1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticket2))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tickets?aplicacion=APP1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].aplicacion", equalTo("APP1")));
    }
}
