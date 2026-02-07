package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.ComentarioDTO;
import com.altamira.incidentes_y_solicitudes.service.ComentarioService;
import com.altamira.incidentes_y_solicitudes.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping
    public List<ComentarioDTO> getAll() {
        return comentarioService.findAll();
    }

    @GetMapping("/{id}")
    public ComentarioDTO getById(@PathVariable Long id) {
        return comentarioService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComentarioDTO create(@Validated(ValidationGroups.Create.class) @RequestBody ComentarioDTO dto) {
        return comentarioService.create(dto);
    }

    @PutMapping("/{id}")
    public ComentarioDTO update(@PathVariable Long id,
                                @Validated(ValidationGroups.Update.class) @RequestBody ComentarioDTO dto) {
        return comentarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        comentarioService.delete(id);
    }

    @GetMapping("/ticket/{ticketId}")
    public List<ComentarioDTO> getByTicket(@PathVariable Long ticketId) {
        return comentarioService.findByTicketId(ticketId);
    }
}
