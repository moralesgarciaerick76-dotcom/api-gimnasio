package com.curso.gimnasio.dto;

import java.math.BigDecimal;

public class GymClassResponse {

    private Long id;
    private String name;
    private String schedule;
    private BigDecimal price;
    private Integer availableSpots;
    private Long trainerId;
    private String trainerName;

    public GymClassResponse() {
    }

    public GymClassResponse(Long id, String name, String schedule, BigDecimal price,
                            Integer availableSpots, Long trainerId, String trainerName) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.price = price;
        this.availableSpots = availableSpots;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}
