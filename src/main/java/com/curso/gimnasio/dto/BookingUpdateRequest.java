package com.curso.gimnasio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Lo único que se puede modificar de una reserva son sus observaciones.
 * Para cambiar las clases o los cupos hay que cancelarla y crear otra.
 */
public class BookingUpdateRequest {

    @NotNull(message = "Las observaciones son obligatorias (pueden ir vacías para borrarlas)")
    @Size(max = 255, message = "Las observaciones no pueden tener más de 255 caracteres")
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
