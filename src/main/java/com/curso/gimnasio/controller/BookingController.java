package com.curso.gimnasio.controller;

import com.curso.gimnasio.dto.BookingRequest;
import com.curso.gimnasio.dto.BookingResponse;
import com.curso.gimnasio.entity.BookingStatus;
import com.curso.gimnasio.service.BookingService;
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

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Reservas")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear reserva (caso de uso central)")
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping
    @Operation(summary = "Listar reservas")
    public List<BookingResponse> findAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por id")
    public BookingResponse findById(@PathVariable Long id) {
        return bookingService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar reserva (si estaba activa, libera los cupos)")
    public void delete(@PathVariable Long id) {
        bookingService.delete(id);
    }

    @GetMapping("/by-member-email")
    @Operation(summary = "Reservas de un socio, por email")
    public List<BookingResponse> findByMemberEmail(@RequestParam String email) {
        return bookingService.findByMemberEmail(email);
    }

    @GetMapping("/by-trainer")
    @Operation(summary = "Reservas que incluyen clases de un entrenador (JPQL con JOIN)")
    public List<BookingResponse> findByTrainer(@RequestParam String trainerName) {
        return bookingService.findByTrainerName(trainerName);
    }

    @GetMapping("/by-status")
    @Operation(summary = "Reservas por estado: ACTIVE o CANCELLED")
    public List<BookingResponse> findByStatus(@RequestParam BookingStatus status) {
        return bookingService.findByStatus(status);
    }
}
