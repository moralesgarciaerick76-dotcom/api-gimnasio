package com.curso.gimnasio.booking.infrastructure.adapter.out;

import com.curso.gimnasio.booking.application.port.out.BookingRepositoryPort;
import com.curso.gimnasio.booking.domain.model.Booking;
import com.curso.gimnasio.booking.domain.model.BookingItem;
import com.curso.gimnasio.booking.domain.model.BookingStatus;
import com.curso.gimnasio.booking.infrastructure.entities.BookingEntity;
import com.curso.gimnasio.booking.infrastructure.entities.BookingItemEntity;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.gymclass.infrastructure.adapter.out.GymClassJpaRepository;
import com.curso.gimnasio.member.infrastructure.adapter.out.MemberJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookingPersistenceAdapter implements BookingRepositoryPort {

    private final BookingJpaRepository bookingJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final GymClassJpaRepository gymClassJpaRepository;

    public BookingPersistenceAdapter(BookingJpaRepository bookingJpaRepository,
                                     MemberJpaRepository memberJpaRepository,
                                     GymClassJpaRepository gymClassJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.gymClassJpaRepository = gymClassJpaRepository;
    }

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity;
        if (booking.getId() == null) {
            entity = new BookingEntity(memberJpaRepository.getReferenceById(booking.getMemberId()),
                    booking.getBookingDate(), booking.getStatus(), booking.getNotes());
            for (BookingItem item : booking.getItems()) {
                entity.addItem(new BookingItemEntity(gymClassJpaRepository.getReferenceById(item.getClassId()),
                        item.getSpots(), item.getUnitPrice()));
            }
        } else {
            entity = bookingJpaRepository.findById(booking.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada: " + booking.getId()));
            entity.setStatus(booking.getStatus());
            entity.setCancelledAt(booking.getCancelledAt());
            entity.setNotes(booking.getNotes());
        }
        return BookingPersistenceMapper.toDomain(bookingJpaRepository.save(entity));
    }

    @Override
    public List<Booking> findAll() {
        return toDomainList(bookingJpaRepository.findAll());
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingJpaRepository.findById(id).map(BookingPersistenceMapper::toDomain);
    }

    @Override
    public List<Booking> findByMemberEmail(String email) {
        return toDomainList(bookingJpaRepository.findByMemberEmailIgnoreCase(email));
    }

    @Override
    public List<Booking> findByTrainerName(String trainerName) {
        return toDomainList(bookingJpaRepository.findBookingsByTrainerName(trainerName));
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return toDomainList(bookingJpaRepository.findByStatus(status));
    }

    @Override
    public long countByMemberAndStatus(Long memberId, BookingStatus status) {
        return bookingJpaRepository.countByMemberIdAndStatus(memberId, status);
    }

    @Override
    public void delete(Booking booking) {
        bookingJpaRepository.deleteById(booking.getId());
    }

    private List<Booking> toDomainList(List<BookingEntity> entities) {
        return entities.stream()
                .map(BookingPersistenceMapper::toDomain)
                .toList();
    }
}
