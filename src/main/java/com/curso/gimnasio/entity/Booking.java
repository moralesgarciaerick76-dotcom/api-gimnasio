package com.curso.gimnasio.entity;

import com.curso.gimnasio.member.infrastructure.entities.MemberEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Reserva: un socio aparta cupos en una o varias clases.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    /** Se llena recién cuando el socio cancela la reserva. */
    private LocalDateTime cancelledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    /** Observaciones libres del socio, por ejemplo "voy con un invitado". */
    @Column(length = 255)
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingItem> items = new ArrayList<>();

    public Booking() {
    }

    public Booking(MemberEntity member, String notes) {
        this.member = member;
        this.notes = notes;
        this.bookingDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.status = BookingStatus.ACTIVE;
    }

    public void addItem(BookingItem item) {
        item.setBooking(this);
        items.add(item);
    }

    public boolean isActive() {
        return status == BookingStatus.ACTIVE;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public List<BookingItem> getItems() {
        return items;
    }

    public void setItems(List<BookingItem> items) {
        this.items = items;
    }
}
