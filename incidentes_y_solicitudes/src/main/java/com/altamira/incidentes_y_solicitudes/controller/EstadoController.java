package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.persistence.dto.EstadoDTO;
import com.altamira.incidentes_y_solicitudes.service.EstadoService;
import com.altamira.incidentes_y_solicitudes.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    public List<EstadoDTO> getAll() {
        return estadoService.findAll();
    }

    @GetMapping("/{id}")
    public EstadoDTO getById(@PathVariable Integer id) {
        return estadoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoDTO create(@Validated(ValidationGroups.Create.class) @RequestBody EstadoDTO dto) {
        return estadoService.create(dto);
    }

    @PutMapping("/{id}")
    public EstadoDTO update(@PathVariable Integer id,
                            @Validated(ValidationGroups.Update.class) @RequestBody EstadoDTO dto) {
        return estadoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        estadoService.delete(id);
    }
}
