package com.curso.gimnasio.booking.application.port.in;

public class BookingItemCommand {

    private Long classId;
    private Integer spots;

    public BookingItemCommand(Long classId, Integer spots) {
        this.classId = classId;
        this.spots = spots;
    }

    public Long getClassId() {
        return classId;
    }

    public Integer getSpots() {
        return spots;
    }
}
