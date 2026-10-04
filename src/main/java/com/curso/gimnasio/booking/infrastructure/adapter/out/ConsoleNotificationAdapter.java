package com.curso.gimnasio.booking.infrastructure.adapter.out;

import com.curso.gimnasio.booking.application.port.out.BookingNotificationPort;
import com.curso.gimnasio.booking.domain.model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleNotificationAdapter implements BookingNotificationPort {

    private static final Logger log = LoggerFactory.getLogger(ConsoleNotificationAdapter.class);

    @Override
    public void sendConfirmation(Booking booking) {
        log.info("Enviando email de confirmación a {} por la reserva #{}", booking.getMemberEmail(), booking.getId());
    }

    @Override
    public void generateReceipt(Booking booking) {
        log.info("Generando comprobante para la reserva #{}", booking.getId());
    }
}
