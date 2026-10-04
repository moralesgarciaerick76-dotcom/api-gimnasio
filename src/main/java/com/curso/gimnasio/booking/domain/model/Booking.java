package com.curso.gimnasio.booking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Booking {

    private Long id;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private LocalDateTime cancelledAt;
    private String notes;
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private final List<BookingItem> items = new ArrayList<>();

    public Booking(Long id, LocalDateTime bookingDate, BookingStatus status, LocalDateTime cancelledAt,
                   String notes, Long memberId, String memberName, String memberEmail) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
        this.cancelledAt = cancelledAt;
        this.notes = notes;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public static Booking create(Long memberId, String memberName, String memberEmail, String notes) {
        return new Booking(null, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), BookingStatus.ACTIVE,
                null, notes, memberId, memberName, memberEmail);
    }

    public void addItem(BookingItem item) {
        items.add(item);
    }

    public boolean isActive() {
        return status == BookingStatus.ACTIVE;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public int getTotalSpots() {
        return items.stream()
                .mapToInt(BookingItem::getSpots)
                .sum();
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(BookingItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public List<BookingItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
