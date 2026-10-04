package com.curso.gimnasio.booking.application.port.in;

import java.util.List;

public class CreateBookingCommand {

    private Long memberId;
    private String notes;
    private List<BookingItemCommand> items;

    public CreateBookingCommand(Long memberId, String notes, List<BookingItemCommand> items) {
        this.memberId = memberId;
        this.notes = notes;
        this.items = items;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getNotes() {
        return notes;
    }

    public List<BookingItemCommand> getItems() {
        return items;
    }
}
