package com.curso.gimnasio.entity;

import com.curso.gimnasio.gymclass.infrastructure.entities.GymClassEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

/**
 * TABLA INTERMEDIA entre Booking y GymClass.
 *
 * Una reserva puede incluir muchas clases y una misma clase aparece en muchas reservas:
 * es una relación muchos a muchos (N:M). En vez de usar @ManyToMany, la tabla intermedia
 * se modela como entidad propia porque necesita guardar datos de la relación: cuántos
 * cupos se reservaron (spots) y a qué precio estaba el cupo en ese momento (unitPrice).
 *
 * bookings 1 ── * booking_items * ── 1 gym_classes
 *
 * La restricción única (booking_id, gym_class_id) garantiza que una clase aparezca una
 * sola vez por reserva.
 */
@Entity
@Table(name = "booking_items",
       uniqueConstraints = @UniqueConstraint(name = "uk_booking_class", columnNames = {"booking_id", "gym_class_id"}))
public class BookingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cupos reservados en esa clase (el socio puede llevar invitados). */
    @Column(nullable = false)
    private Integer spots;

    /** Precio del cupo al momento de reservar: si la clase sube de precio después, la reserva no cambia. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gym_class_id", nullable = false)
    private GymClassEntity gymClass;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public BookingItem() {
    }

    public BookingItem(GymClassEntity gymClass, Integer spots) {
        this.gymClass = gymClass;
        this.spots = spots;
        this.unitPrice = gymClass.getPrice();
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(spots));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSpots() {
        return spots;
    }

    public void setSpots(Integer spots) {
        this.spots = spots;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public GymClassEntity getGymClass() {
        return gymClass;
    }

    public void setGymClass(GymClassEntity gymClass) {
        this.gymClass = gymClass;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
