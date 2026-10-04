package com.curso.gimnasio.booking.infrastructure.adapter.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class BookingRequest {

    @NotNull(message = "El socio es obligatorio")
    private Long memberId;

    @Size(max = 255, message = "Las observaciones no pueden tener más de 255 caracteres")
    private String notes;

    @NotEmpty(message = "La reserva debe tener al menos una clase")
    @Size(max = 20, message = "Una reserva no puede tener más de 20 clases distintas")
    @Valid
    private List<BookingItemRequest> items;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<BookingItemRequest> getItems() {
        return items;
    }

    public void setItems(List<BookingItemRequest> items) {
        this.items = items;
    }
}
