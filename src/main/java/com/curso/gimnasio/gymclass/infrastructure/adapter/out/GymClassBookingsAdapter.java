package com.curso.gimnasio.gymclass.infrastructure.adapter.out;

import com.curso.gimnasio.booking.infrastructure.adapter.out.BookingJpaRepository;
import com.curso.gimnasio.gymclass.application.port.out.GymClassBookingsPort;
import org.springframework.stereotype.Component;

@Component
public class GymClassBookingsAdapter implements GymClassBookingsPort {

    private final BookingJpaRepository bookingJpaRepository;

    public GymClassBookingsAdapter(BookingJpaRepository bookingJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
    }

    @Override
    public boolean isInBookings(Long gymClassId) {
        return bookingJpaRepository.existsByItemsGymClassId(gymClassId);
    }
}
