package com.curso.gimnasio.booking.infrastructure.adapter.in;

import java.math.BigDecimal;

public class BookingItemResponse {

    private Long classId;
    private String className;
    private Integer spots;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public BookingItemResponse() {
    }

    public BookingItemResponse(Long classId, String className, Integer spots, BigDecimal unitPrice, BigDecimal subtotal) {
        this.classId = classId;
        this.className = className;
        this.spots = spots;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
