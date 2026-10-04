package com.curso.gimnasio.booking.infrastructure.adapter.out;

import com.curso.gimnasio.booking.domain.model.Booking;
import com.curso.gimnasio.booking.domain.model.BookingItem;
import com.curso.gimnasio.booking.infrastructure.entities.BookingEntity;
import com.curso.gimnasio.booking.infrastructure.entities.BookingItemEntity;

public class BookingPersistenceMapper {

    private BookingPersistenceMapper() {
    }

    public static Booking toDomain(BookingEntity entity) {
        Booking booking = new Booking(
                entity.getId(),
                entity.getBookingDate(),
                entity.getStatus(),
                entity.getCancelledAt(),
                entity.getNotes(),
                entity.getMember().getId(),
                entity.getMember().getName(),
                entity.getMember().getEmail()
        );
        for (BookingItemEntity item : entity.getItems()) {
            booking.addItem(new BookingItem(
                    item.getGymClass().getId(),
                    item.getGymClass().getName(),
                    item.getSpots(),
                    item.getUnitPrice()
            ));
        }
        return booking;
    }
}
