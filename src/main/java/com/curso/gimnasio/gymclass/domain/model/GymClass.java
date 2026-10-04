package com.curso.gimnasio.gymclass.domain.model;

import java.math.BigDecimal;

public class GymClass {

    private Long id;
    private String name;
    private String schedule;
    private BigDecimal price;
    private Integer availableSpots;
    private Long trainerId;
    private String trainerName;

    public GymClass() {
    }

    public GymClass(Long id, String name, String schedule, BigDecimal price, Integer availableSpots,
                    Long trainerId, String trainerName) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.price = price;
        this.availableSpots = availableSpots;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
    }

    public boolean hasSpots(int spots) {
        return spots > 0 && availableSpots != null && availableSpots >= spots;
    }

    public void reserveSpots(int spots) {
        if (!hasSpots(spots)) {
            throw new IllegalStateException("Cupos insuficientes en: " + name);
        }
        availableSpots = availableSpots - spots;
    }

    public void releaseSpots(int spots) {
        if (spots <= 0) {
            throw new IllegalArgumentException("La cantidad de cupos a liberar debe ser mayor a 0");
        }
        availableSpots = availableSpots + spots;
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
