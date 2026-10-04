package com.curso.gimnasio.gymclass.infrastructure.adapter.in;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class GymClassRequest {

    @NotBlank(message = "El nombre de la clase es obligatorio")
    @Size(max = 120, message = "El nombre no puede tener más de 120 caracteres")
    private String name;

    @NotBlank(message = "El horario es obligatorio")
    @Size(max = 80, message = "El horario no puede tener más de 80 caracteres")
    private String schedule;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El precio admite como máximo 2 decimales")
    private BigDecimal price;

    @NotNull(message = "Los cupos disponibles son obligatorios")
    @PositiveOrZero(message = "Los cupos no pueden ser negativos")
    @Max(value = 1000, message = "Una clase no puede tener más de 1000 cupos")
    private Integer availableSpots;

    @NotNull(message = "El entrenador es obligatorio")
    private Long trainerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }
}
