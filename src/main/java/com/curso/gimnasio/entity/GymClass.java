package com.curso.gimnasio.entity;

import com.curso.gimnasio.trainer.infrastructure.entities.TrainerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Clase grupal del gimnasio (Yoga, Spinning...). Se llama GymClass porque "Class" es de Java.
 */
@Entity
@Table(name = "gym_classes")
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    /** Días y hora en texto, por ejemplo "Lun-Mié-Vie 07:00". */
    @Column(nullable = false, length = 80)
    private String schedule;

    /** Precio por cupo. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Cupos que todavía se pueden reservar. */
    @Column(nullable = false)
    private Integer availableSpots;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    private TrainerEntity trainer;

    public GymClass() {
    }

    public GymClass(String name, String schedule, BigDecimal price, Integer availableSpots, TrainerEntity trainer) {
        this.name = name;
        this.schedule = schedule;
        this.price = price;
        this.availableSpots = availableSpots;
        this.trainer = trainer;
    }

    /** true si quedan al menos "spots" cupos libres. */
    public boolean hasSpots(int spots) {
        return spots > 0 && availableSpots != null && availableSpots >= spots;
    }

    /** Se llama al reservar. Nunca deja los cupos en negativo. */
    public void reserveSpots(int spots) {
        if (!hasSpots(spots)) {
            throw new IllegalStateException("Cupos insuficientes en: " + name);
        }
        availableSpots = availableSpots - spots;
    }

    /** Se llama cuando una reserva se cancela o se elimina: los cupos vuelven a estar libres. */
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

    public TrainerEntity getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerEntity trainer) {
        this.trainer = trainer;
    }
}
