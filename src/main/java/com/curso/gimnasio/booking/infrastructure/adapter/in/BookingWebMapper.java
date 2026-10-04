package com.curso.gimnasio.booking.infrastructure.adapter.in;

import com.curso.gimnasio.booking.application.port.in.BookingItemCommand;
import com.curso.gimnasio.booking.application.port.in.CreateBookingCommand;
import com.curso.gimnasio.booking.domain.model.Booking;

import java.util.List;

public class BookingWebMapper {

    private BookingWebMapper() {
    }

    public static BookingResponse toResponse(Booking booking) {
        List<BookingItemResponse> items = booking.getItems().stream()
                .map(item -> new BookingItemResponse(
                        item.getClassId(),
                        item.getClassName(),
                        item.getSpots(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .toList();

        return new BookingResponse(
                booking.getId(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getCancelledAt(),
                booking.getNotes(),
                booking.getMemberId(),
                booking.getMemberName(),
                items,
                booking.getTotalSpots(),
                booking.getTotal()
        );
    }

    public static List<BookingResponse> toResponseList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingWebMapper::toResponse)
                .toList();
    }

    public static CreateBookingCommand toCommand(BookingRequest request) {
        List<BookingItemCommand> items = request.getItems().stream()
                .map(item -> new BookingItemCommand(item.getClassId(), item.getSpots()))
                .toList();
        return new CreateBookingCommand(request.getMemberId(), request.getNotes(), items);
    }
}
