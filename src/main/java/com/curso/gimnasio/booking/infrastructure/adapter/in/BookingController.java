package com.curso.gimnasio.booking.infrastructure.adapter.in;

import com.curso.gimnasio.booking.application.port.in.CancelBookingUseCase;
import com.curso.gimnasio.booking.application.port.in.CreateBookingUseCase;
import com.curso.gimnasio.booking.application.port.in.DeleteBookingUseCase;
import com.curso.gimnasio.booking.application.port.in.GetBookingUseCase;
import com.curso.gimnasio.booking.domain.model.BookingStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private final CreateBookingUseCase createBookingUseCase;
    private final GetBookingUseCase getBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final DeleteBookingUseCase deleteBookingUseCase;

    public BookingController(CreateBookingUseCase createBookingUseCase,
                             GetBookingUseCase getBookingUseCase,
                             CancelBookingUseCase cancelBookingUseCase,
                             DeleteBookingUseCase deleteBookingUseCase) {
        this.createBookingUseCase = createBookingUseCase;
        this.getBookingUseCase = getBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
        this.deleteBookingUseCase = deleteBookingUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear reserva (caso de uso central)")
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        return BookingWebMapper.toResponse(createBookingUseCase.create(BookingWebMapper.toCommand(request)));
    }

    @GetMapping
    @Operation(summary = "Listar reservas")
    public List<BookingResponse> findAll() {
        return BookingWebMapper.toResponseList(getBookingUseCase.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por id")
    public BookingResponse findById(@PathVariable Long id) {
        return BookingWebMapper.toResponse(getBookingUseCase.findById(id));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelar reserva (pasa a CANCELLED, registra la fecha y libera los cupos)")
    public BookingResponse cancel(@PathVariable Long id) {
        return BookingWebMapper.toResponse(cancelBookingUseCase.cancel(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar reserva (si estaba activa, libera los cupos)")
    public void delete(@PathVariable Long id) {
        deleteBookingUseCase.delete(id);
    }

    @GetMapping("/by-member-email")
    @Operation(summary = "Reservas de un socio, por email")
    public List<BookingResponse> findByMemberEmail(@RequestParam String email) {
        return BookingWebMapper.toResponseList(getBookingUseCase.findByMemberEmail(email));
    }

    @GetMapping("/by-trainer")
    @Operation(summary = "Reservas que incluyen clases de un entrenador (JPQL con JOIN)")
    public List<BookingResponse> findByTrainer(@RequestParam String trainerName) {
        return BookingWebMapper.toResponseList(getBookingUseCase.findByTrainerName(trainerName));
    }

    @GetMapping("/by-status")
    @Operation(summary = "Reservas por estado: ACTIVE o CANCELLED")
    public List<BookingResponse> findByStatus(@RequestParam BookingStatus status) {
        return BookingWebMapper.toResponseList(getBookingUseCase.findByStatus(status));
    }
}
