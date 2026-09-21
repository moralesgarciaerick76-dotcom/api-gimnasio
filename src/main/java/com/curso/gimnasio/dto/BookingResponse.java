package com.curso.gimnasio.dto;

import com.curso.gimnasio.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {

    private Long id;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private LocalDateTime cancelledAt;
    private String notes;
    private Long memberId;
    private String memberName;
    private List<BookingItemResponse> items;
    private Integer totalSpots;
    private BigDecimal total;

    public BookingResponse() {
    }

    public BookingResponse(Long id, LocalDateTime bookingDate, BookingStatus status, LocalDateTime cancelledAt,
                           String notes, Long memberId, String memberName,
                           List<BookingItemResponse> items, Integer totalSpots, BigDecimal total) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
        this.cancelledAt = cancelledAt;
        this.notes = notes;
        this.memberId = memberId;
        this.memberName = memberName;
        this.items = items;
        this.totalSpots = totalSpots;
        this.total = total;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
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

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public List<BookingItemResponse> getItems() {
        return items;
    }

    public void setItems(List<BookingItemResponse> items) {
        this.items = items;
    }

    public Integer getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(Integer totalSpots) {
        this.totalSpots = totalSpots;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
