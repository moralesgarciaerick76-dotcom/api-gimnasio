package com.curso.gimnasio.booking.infrastructure.entities;

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

@Entity(name = "BookingItem")
@Table(name = "booking_items",
       uniqueConstraints = @UniqueConstraint(name = "uk_booking_class", columnNames = {"booking_id", "gym_class_id"}))
public class BookingItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer spots;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gym_class_id", nullable = false)
    private GymClassEntity gymClass;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingEntity booking;

    public BookingItemEntity() {
    }

    public BookingItemEntity(GymClassEntity gymClass, Integer spots, BigDecimal unitPrice) {
        this.gymClass = gymClass;
        this.spots = spots;
        this.unitPrice = unitPrice;
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

    public BookingEntity getBooking() {
        return booking;
    }

    public void setBooking(BookingEntity booking) {
        this.booking = booking;
    }
}
