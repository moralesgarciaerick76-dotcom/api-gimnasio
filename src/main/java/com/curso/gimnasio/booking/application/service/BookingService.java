package com.curso.gimnasio.booking.application.service;

import com.curso.gimnasio.booking.application.port.in.BookingItemCommand;
import com.curso.gimnasio.booking.application.port.in.CreateBookingCommand;
import com.curso.gimnasio.booking.application.port.in.CreateBookingUseCase;
import com.curso.gimnasio.booking.application.port.in.DeleteBookingUseCase;
import com.curso.gimnasio.booking.application.port.in.GetBookingUseCase;
import com.curso.gimnasio.booking.application.port.out.BookingClassPort;
import com.curso.gimnasio.booking.application.port.out.BookingMemberPort;
import com.curso.gimnasio.booking.application.port.out.BookingNotificationPort;
import com.curso.gimnasio.booking.application.port.out.BookingRepositoryPort;
import com.curso.gimnasio.booking.domain.model.Booking;
import com.curso.gimnasio.booking.domain.model.BookingItem;
import com.curso.gimnasio.booking.domain.model.BookingStatus;
import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.gymclass.domain.model.GymClass;
import com.curso.gimnasio.member.domain.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional(readOnly = true)
public class BookingService implements CreateBookingUseCase, GetBookingUseCase, DeleteBookingUseCase {

    static final int MAX_ACTIVE_BOOKINGS_PER_MEMBER = 3;

    private final BookingRepositoryPort repository;
    private final BookingMemberPort memberPort;
    private final BookingClassPort classPort;
    private final BookingNotificationPort notificationPort;

    public BookingService(BookingRepositoryPort repository,
                          BookingMemberPort memberPort,
                          BookingClassPort classPort,
                          BookingNotificationPort notificationPort) {
        this.repository = repository;
        this.memberPort = memberPort;
        this.classPort = classPort;
        this.notificationPort = notificationPort;
    }

    @Override
    @Transactional
    public Booking create(CreateBookingCommand command) {
        Map<Long, Integer> spotsByClass = groupSpotsByClass(command.getItems());

        Member member = memberPort.findById(command.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado: " + command.getMemberId()));

        long activeBookings = repository.countByMemberAndStatus(member.getId(), BookingStatus.ACTIVE);
        if (activeBookings >= MAX_ACTIVE_BOOKINGS_PER_MEMBER) {
            throw new BusinessRuleException("El socio " + member.getName() + " ya tiene " + activeBookings
                    + " reservas activas (máximo " + MAX_ACTIVE_BOOKINGS_PER_MEMBER + ")");
        }

        Booking booking = Booking.create(member.getId(), member.getName(), member.getEmail(),
                cleanNotes(command.getNotes()));

        for (Map.Entry<Long, Integer> entry : spotsByClass.entrySet()) {
            Long classId = entry.getKey();
            int spots = entry.getValue();

            GymClass gymClass = classPort.findByIdForUpdate(classId)
                    .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada: " + classId));

            if (!gymClass.hasSpots(spots)) {
                throw new BusinessRuleException("Cupos insuficientes para: " + gymClass.getName()
                        + " (disponibles: " + gymClass.getAvailableSpots() + ", pedidos: " + spots + ")");
            }

            gymClass.reserveSpots(spots);
            classPort.updateAvailableSpots(gymClass);

            booking.addItem(new BookingItem(gymClass.getId(), gymClass.getName(), spots, gymClass.getPrice()));
        }

        Booking saved = repository.save(booking);

        notificationPort.sendConfirmation(saved);
        notificationPort.generateReceipt(saved);

        return saved;
    }

    @Override
    public Booking findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada: " + id));
    }

    @Override
    public List<Booking> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Booking> findByMemberEmail(String email) {
        return repository.findByMemberEmail(email.trim());
    }

    @Override
    public List<Booking> findByTrainerName(String trainerName) {
        return repository.findByTrainerName(trainerName.trim());
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Booking booking = findById(id);
        if (booking.isActive()) {
            releaseSpots(booking);
        }
        repository.delete(booking);
    }

    private Map<Long, Integer> groupSpotsByClass(List<BookingItemCommand> items) {
        Map<Long, Integer> spotsByClass = new TreeMap<>();
        for (BookingItemCommand item : items) {
            spotsByClass.merge(item.getClassId(), item.getSpots(), Integer::sum);
        }
        return spotsByClass;
    }

    private void releaseSpots(Booking booking) {
        List<BookingItem> items = booking.getItems().stream()
                .sorted((a, b) -> Long.compare(a.getClassId(), b.getClassId()))
                .toList();
        for (BookingItem item : items) {
            GymClass gymClass = classPort.findById(item.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada: " + item.getClassId()));
            gymClass.releaseSpots(item.getSpots());
            classPort.updateAvailableSpots(gymClass);
        }
    }

    private String cleanNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }
        return notes.trim();
    }
}
