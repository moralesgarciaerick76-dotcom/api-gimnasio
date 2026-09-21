package com.curso.gimnasio.controller;

import com.curso.gimnasio.dto.TrainerDto;
import com.curso.gimnasio.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Entrenadores")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar entrenador")
    public TrainerDto create(@Valid @RequestBody TrainerDto request) {
        return trainerService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar entrenadores")
    public List<TrainerDto> findAll() {
        return trainerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar entrenador por id")
    public TrainerDto findById(@PathVariable Long id) {
        return trainerService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar entrenador (solo si no tiene clases asignadas)")
    public void delete(@PathVariable Long id) {
        trainerService.delete(id);
    }
}
