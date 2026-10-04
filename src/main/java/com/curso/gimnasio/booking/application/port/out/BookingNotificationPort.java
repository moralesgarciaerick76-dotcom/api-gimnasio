package com.curso.gimnasio.booking.application.port.out;

import com.curso.gimnasio.booking.domain.model.Booking;

public interface BookingNotificationPort {

    void sendConfirmation(Booking booking);

    void generateReceipt(Booking booking);
}
