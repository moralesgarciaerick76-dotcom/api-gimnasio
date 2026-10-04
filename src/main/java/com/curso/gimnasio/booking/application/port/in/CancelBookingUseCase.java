package com.curso.gimnasio.booking.application.port.in;

import com.curso.gimnasio.booking.domain.model.Booking;

public interface CancelBookingUseCase {

    Booking cancel(Long id);
}
