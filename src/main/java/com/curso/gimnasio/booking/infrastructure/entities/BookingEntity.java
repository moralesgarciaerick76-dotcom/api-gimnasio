package com.curso.gimnasio.booking.infrastructure.entities;

import com.curso.gimnasio.booking.domain.model.BookingStatus;
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
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Booking")
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    private LocalDateTime cancelledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(length = 255)
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingItemEntity> items = new ArrayList<>();

    public BookingEntity() {
    }

    public BookingEntity(MemberEntity member, LocalDateTime bookingDate, BookingStatus status, String notes) {
        this.member = member;
        this.bookingDate = bookingDate;
        this.status = status;
        this.notes = notes;
    }

    public void addItem(BookingItemEntity item) {
        item.setBooking(this);
        items.add(item);
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

    public List<BookingItemEntity> getItems() {
        return items;
    }

    public void setItems(List<BookingItemEntity> items) {
        this.items = items;
    }
}
