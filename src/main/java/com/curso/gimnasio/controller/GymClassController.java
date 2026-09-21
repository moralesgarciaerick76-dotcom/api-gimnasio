package com.curso.gimnasio.controller;

import com.curso.gimnasio.dto.GymClassRequest;
import com.curso.gimnasio.dto.GymClassResponse;
import com.curso.gimnasio.service.GymClassService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
@Tag(name = "Clases")
public class GymClassController {

    private final GymClassService gymClassService;

    public GymClassController(GymClassService gymClassService) {
        this.gymClassService = gymClassService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar clase")
    public GymClassResponse create(@Valid @RequestBody GymClassRequest request) {
        return gymClassService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar clases")
    public List<GymClassResponse> findAll() {
        return gymClassService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar clase por id")
    public GymClassResponse findById(@PathVariable Long id) {
        return gymClassService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar clase (solo si no aparece en reservas)")
    public void delete(@PathVariable Long id) {
        gymClassService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar por nombre (contiene, sin distinguir mayúsculas)")
    public List<GymClassResponse> searchByName(@RequestParam String name) {
        return gymClassService.searchByName(name);
    }

    @GetMapping("/by-schedule")
    @Operation(summary = "Buscar por horario, por ejemplo 'lun' o '19:00'")
    public List<GymClassResponse> findBySchedule(@RequestParam String schedule) {
        return gymClassService.findBySchedule(schedule);
    }

    @GetMapping("/filter")
    @Operation(summary = "Clases hasta un precio máximo (inclusive) y con un mínimo de cupos libres (inclusive)")
    public List<GymClassResponse> filter(@RequestParam BigDecimal maxPrice, @RequestParam Integer minSpots) {
        return gymClassService.filterByPriceAndSpots(maxPrice, minSpots);
    }

    @GetMapping("/available")
    @Operation(summary = "Clases con al menos un cupo libre")
    public List<GymClassResponse> findAvailable() {
        return gymClassService.findAvailable();
    }

    @GetMapping("/by-trainer")
    @Operation(summary = "Clases de un entrenador, por nombre")
    public List<GymClassResponse> findByTrainer(@RequestParam String trainerName) {
        return gymClassService.findByTrainerName(trainerName);
    }
}
