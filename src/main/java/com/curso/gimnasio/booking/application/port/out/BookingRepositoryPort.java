package com.curso.gimnasio.booking.application.port.out;

import com.curso.gimnasio.booking.domain.model.Booking;
import com.curso.gimnasio.booking.domain.model.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepositoryPort {

    Booking save(Booking booking);

    List<Booking> findAll();

    Optional<Booking> findById(Long id);

    List<Booking> findByMemberEmail(String email);

    List<Booking> findByTrainerName(String trainerName);

    List<Booking> findByStatus(BookingStatus status);

    long countByMemberAndStatus(Long memberId, BookingStatus status);

    void delete(Booking booking);
}
