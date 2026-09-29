package com.curso.gimnasio.service;

import com.curso.gimnasio.dto.BookingItemRequest;
import com.curso.gimnasio.dto.BookingItemResponse;
import com.curso.gimnasio.dto.BookingRequest;
import com.curso.gimnasio.dto.BookingResponse;
import com.curso.gimnasio.entity.Booking;
import com.curso.gimnasio.entity.BookingItem;
import com.curso.gimnasio.entity.BookingStatus;
import com.curso.gimnasio.entity.GymClass;
import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.member.infrastructure.adapter.out.MemberJpaRepository;
import com.curso.gimnasio.member.infrastructure.entities.MemberEntity;
import com.curso.gimnasio.repository.BookingRepository;
import com.curso.gimnasio.repository.GymClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Servicio de reservas: el caso de uso central de la API.
 *
 * OJO: a propósito, esta clase hace demasiadas cosas. Además de crear la reserva,
 * valida las reglas de negocio, descuenta los cupos, "envía" el email, "genera" el
 * comprobante y habla directamente con los JpaRepository. Es el equivalente al
 * OrderService del proyecto del curso: el punto de partida para aplicar SRP / DIP
 * y luego Arquitectura Hexagonal en las próximas clases. No refactorizar antes.
 */
@Service
@Transactional(readOnly = true)
public class BookingService {

    /** Un socio no puede tener más de esta cantidad de reservas vigentes. */
    static final int MAX_ACTIVE_BOOKINGS_PER_MEMBER = 3;

    private final BookingRepository bookingRepository;
    private final MemberJpaRepository memberRepository;
    private final GymClassRepository gymClassRepository;

    public BookingService(BookingRepository bookingRepository,
                          MemberJpaRepository memberRepository,
                          GymClassRepository gymClassRepository) {
        this.bookingRepository = bookingRepository;
        this.memberRepository = memberRepository;
        this.gymClassRepository = gymClassRepository;
    }

    /**
     * Todo el método corre en UNA sola transacción: si una clase no tiene cupos,
     * se hace rollback y tampoco se descuentan los cupos de las clases anteriores.
     * Ojo: sin @Transactional este método deja de funcionar (el bloqueo lo exige).
     */
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        MemberEntity member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado: " + request.getMemberId()));

        long activeBookings = bookingRepository.countByMemberIdAndStatus(member.getId(), BookingStatus.ACTIVE);
        if (activeBookings >= MAX_ACTIVE_BOOKINGS_PER_MEMBER) {
            throw new BusinessRuleException("El socio " + member.getName() + " ya tiene " + activeBookings
                    + " reservas activas (máximo " + MAX_ACTIVE_BOOKINGS_PER_MEMBER + ")");
        }

        Booking booking = new Booking(member, cleanNotes(request.getNotes()));

        // Si la misma clase viene repetida en la petición, se suman sus cupos:
        // en la tabla intermedia cada clase aparece una sola vez por reserva.
        // TreeMap las deja ordenadas por id: todas las reservas bloquean las clases
        // en el mismo orden y así dos peticiones simultáneas no se traban entre sí.
        Map<Long, Integer> spotsByClass = new TreeMap<>();
        for (BookingItemRequest itemRequest : request.getItems()) {
            spotsByClass.merge(itemRequest.getClassId(), itemRequest.getSpots(), Integer::sum);
        }

        for (Map.Entry<Long, Integer> entry : spotsByClass.entrySet()) {
            Long classId = entry.getKey();
            int spots = entry.getValue();

            // findByIdForUpdate bloquea la fila de la clase: evita vender dos veces el mismo cupo
            GymClass gymClass = gymClassRepository.findByIdForUpdate(classId)
                    .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada: " + classId));

            if (!gymClass.hasSpots(spots)) {
                throw new BusinessRuleException("Cupos insuficientes para: " + gymClass.getName()
                        + " (disponibles: " + gymClass.getAvailableSpots() + ", pedidos: " + spots + ")");
            }

            gymClass.reserveSpots(spots);
            gymClassRepository.save(gymClass);

            booking.addItem(new BookingItem(gymClass, spots));
        }

        Booking saved = bookingRepository.save(booking);

        sendConfirmationEmail(saved);
        generateBookingReceipt(saved);

        return toResponse(saved);
    }

    public List<BookingResponse> findAll() {
        return toResponseList(bookingRepository.findAll());
    }

    public BookingResponse findById(Long id) {
        return toResponse(findBooking(id));
    }

    public List<BookingResponse> findByMemberEmail(String email) {
        return toResponseList(bookingRepository.findByMemberEmailIgnoreCase(email.trim()));
    }

    public List<BookingResponse> findByTrainerName(String trainerName) {
        return toResponseList(bookingRepository.findBookingsByTrainerName(trainerName.trim()));
    }

    public List<BookingResponse> findByStatus(BookingStatus status) {
        return toResponseList(bookingRepository.findByStatus(status));
    }

    /** Si se elimina una reserva que seguía activa, sus cupos vuelven a quedar libres. */
    @Transactional
    public void delete(Long id) {
        Booking booking = findBooking(id);
        if (booking.isActive()) {
            releaseSpots(booking);
        }
        bookingRepository.delete(booking);
    }

    // --- esto no debería vivir aquí: es la responsabilidad que se separa en las próximas clases ---

    private void sendConfirmationEmail(Booking booking) {
        System.out.println("Enviando email de confirmación a " + booking.getMember().getEmail()
                + " por la reserva #" + booking.getId());
    }

    private void generateBookingReceipt(Booking booking) {
        System.out.println("Generando comprobante para la reserva #" + booking.getId());
    }

    // --- helpers ---

    private void releaseSpots(Booking booking) {
        for (BookingItem item : booking.getItems()) {
            GymClass gymClass = item.getGymClass();
            gymClass.releaseSpots(item.getSpots());
            gymClassRepository.save(gymClass);
        }
    }

    /** Observaciones vacías o solo con espacios se guardan como null. */
    private String cleanNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }
        return notes.trim();
    }

    private Booking findBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada: " + id));
    }

    private List<BookingResponse> toResponseList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        List<BookingItemResponse> items = booking.getItems().stream()
                .map(item -> new BookingItemResponse(
                        item.getGymClass().getId(),
                        item.getGymClass().getName(),
                        item.getSpots(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .toList();

        int totalSpots = items.stream()
                .mapToInt(BookingItemResponse::getSpots)
                .sum();

        BigDecimal total = items.stream()
                .map(BookingItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BookingResponse(
                booking.getId(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getCancelledAt(),
                booking.getNotes(),
                booking.getMember().getId(),
                booking.getMember().getName(),
                items,
                totalSpots,
                total
        );
    }
}
