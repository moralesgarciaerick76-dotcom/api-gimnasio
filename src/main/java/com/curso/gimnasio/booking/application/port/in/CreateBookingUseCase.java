package com.curso.gimnasio.booking.application.port.in;

import com.curso.gimnasio.booking.domain.model.Booking;

public interface CreateBookingUseCase {

    Booking create(CreateBookingCommand command);
}
