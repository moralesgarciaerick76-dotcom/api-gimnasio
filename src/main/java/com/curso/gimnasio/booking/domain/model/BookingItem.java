package com.curso.gimnasio.booking.domain.model;

import java.math.BigDecimal;

public class BookingItem {

    private Long classId;
    private String className;
    private Integer spots;
    private BigDecimal unitPrice;

    public BookingItem(Long classId, String className, Integer spots, BigDecimal unitPrice) {
        this.classId = classId;
        this.className = className;
        this.spots = spots;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(spots));
    }

    public Long getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public Integer getSpots() {
        return spots;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
