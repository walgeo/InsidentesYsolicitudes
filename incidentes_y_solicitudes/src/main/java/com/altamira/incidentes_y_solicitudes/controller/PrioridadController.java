package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.PrioridadDTO;
import com.altamira.incidentes_y_solicitudes.service.PrioridadService;
import com.altamira.incidentes_y_solicitudes.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prioridades")
@RequiredArgsConstructor
public class PrioridadController {

    private final PrioridadService prioridadService;

    @GetMapping
    public List<PrioridadDTO> getAll() {
        return prioridadService.findAll();
    }

    @GetMapping("/{id}")
    public PrioridadDTO getById(@PathVariable Integer id) {
        return prioridadService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrioridadDTO create(@Validated(ValidationGroups.Create.class) @RequestBody PrioridadDTO dto) {
        return prioridadService.create(dto);
    }

    @PutMapping("/{id}")
    public PrioridadDTO update(@PathVariable Integer id,
                               @Validated(ValidationGroups.Update.class) @RequestBody PrioridadDTO dto) {
        return prioridadService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        prioridadService.delete(id);
    }
}
