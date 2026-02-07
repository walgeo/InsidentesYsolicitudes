package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.TicketDTO;
import com.altamira.incidentes_y_solicitudes.service.TicketService;
import com.altamira.incidentes_y_solicitudes.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public Page<TicketDTO> getAll(@RequestParam(required = false) String aplicacion,
                                  @RequestParam(required = false) Integer estadoId,
                                  @RequestParam(required = false) Integer prioridadId,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creadoDesde,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creadoHasta,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(required = false) List<String> sort) {
        if (size < 1 || size > 100) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "size debe estar entre 1 y 100");
        }
        Sort sortSpec = parseSort(sort);
        return ticketService.findAll(aplicacion, estadoId, prioridadId, creadoDesde, creadoHasta,
                PageRequest.of(page, size, sortSpec));
    }

    @GetMapping("/{id}")
    public TicketDTO getById(@PathVariable Long id) {
        return ticketService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDTO create(@Validated(ValidationGroups.Create.class) @RequestBody TicketDTO dto) {
        return ticketService.create(dto);
    }

    @PutMapping("/{id}")
    public TicketDTO update(@PathVariable Long id,
                            @Validated(ValidationGroups.Update.class) @RequestBody TicketDTO dto) {
        return ticketService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }

    private Sort parseSort(List<String> sort) {
        if (sort == null || sort.isEmpty()) {
            log.info("📊 No sort provided, using default: creadoEn DESC");
            return Sort.by(Sort.Order.desc("creadoEn"));
        }

        log.info("📊 Parsing sort: {}", sort);
        Sort result = Sort.unsorted();

        for (String value : sort) {
            if (value == null || value.isBlank()) {
                continue;
            }

            log.info("📊 Processing sort value: '{}'", value);
            String[] parts = value.split(",");
            String property = parts[0].trim();
            String direction = parts.length > 1 ? parts[1].trim() : "asc";

            log.info("📊 Property: '{}', Direction: '{}'", property, direction);

            // Validar que la propiedad sea válida
            if (!isValidSortProperty(property)) {
                log.warn("⚠️  Invalid sort property: '{}', ignoring", property);
                continue;
            }

            Sort.Order order = "desc".equalsIgnoreCase(direction)
                    ? Sort.Order.desc(property)
                    : Sort.Order.asc(property);
            result = result.and(Sort.by(order));
        }

        if (result.isUnsorted()) {
            log.info("📊 Result is unsorted, using default: creadoEn DESC");
            return Sort.by(Sort.Order.desc("creadoEn"));
        }

        log.info("✅ Final sort: {}", result);
        return result;
    }

    private boolean isValidSortProperty(String property) {
        // Lista de propiedades válidas para ordenar
        return property.equalsIgnoreCase("id") ||
                property.equalsIgnoreCase("titulo") ||
                property.equalsIgnoreCase("tipo") ||
                property.equalsIgnoreCase("aplicacion") ||
                property.equalsIgnoreCase("prioridadId") ||
                property.equalsIgnoreCase("estadoId") ||
                property.equalsIgnoreCase("creadoEn") ||
                property.equalsIgnoreCase("actualizadoEn") ||
                property.equalsIgnoreCase("creadoPor") ||
                property.equalsIgnoreCase("asignadoA");
    }
}
