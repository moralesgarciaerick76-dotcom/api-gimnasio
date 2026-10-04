package com.curso.gimnasio.booking.application.port.in;

import com.curso.gimnasio.booking.domain.model.Booking;
import com.curso.gimnasio.booking.domain.model.BookingStatus;

import java.util.List;

public interface GetBookingUseCase {

    Booking findById(Long id);

    List<Booking> findAll();

    List<Booking> findByMemberEmail(String email);

    List<Booking> findByTrainerName(String trainerName);

    List<Booking> findByStatus(BookingStatus status);
}
