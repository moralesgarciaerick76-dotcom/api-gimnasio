package com.curso.gimnasio.gymclass.infrastructure.adapter.out;

import com.curso.gimnasio.repository.BookingRepository;
import com.curso.gimnasio.gymclass.application.port.out.GymClassBookingsPort;
import org.springframework.stereotype.Component;

@Component
public class GymClassBookingsAdapter implements GymClassBookingsPort {

    private final BookingRepository bookingRepository;

    public GymClassBookingsAdapter(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean isInBookings(Long gymClassId) {
        return bookingRepository.existsByItemsGymClassId(gymClassId);
    }
}
