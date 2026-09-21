package com.curso.gimnasio.entity;

public enum BookingStatus {

    /** La reserva está vigente: los cupos siguen apartados. */
    ACTIVE,

    /** El socio la canceló: sus cupos volvieron a quedar libres. */
    CANCELLED
}
