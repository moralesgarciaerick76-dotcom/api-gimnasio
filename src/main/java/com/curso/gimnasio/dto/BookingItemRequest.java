package com.curso.gimnasio.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingItemRequest {

    @NotNull(message = "La clase es obligatoria")
    private Long classId;

    @NotNull(message = "La cantidad de cupos es obligatoria")
    @Positive(message = "La cantidad de cupos debe ser mayor a 0")
    @Max(value = 10, message = "No se pueden reservar más de 10 cupos por clase")
    private Integer spots;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getSpots() {
        return spots;
    }

    public void setSpots(Integer spots) {
        this.spots = spots;
    }
}
