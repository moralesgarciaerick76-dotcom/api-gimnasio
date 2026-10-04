package com.curso.gimnasio.gymclass.application.port.in;

import java.math.BigDecimal;

public class CreateGymClassCommand {

    private String name;
    private String schedule;
    private BigDecimal price;
    private Integer availableSpots;
    private Long trainerId;

    public CreateGymClassCommand(String name, String schedule, BigDecimal price, Integer availableSpots, Long trainerId) {
        this.name = name;
        this.schedule = schedule;
        this.price = price;
        this.availableSpots = availableSpots;
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public String getSchedule() {
        return schedule;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public Long getTrainerId() {
        return trainerId;
    }
}
