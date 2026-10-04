package com.curso.gimnasio.booking.application.port.out;

import com.curso.gimnasio.gymclass.domain.model.GymClass;

import java.util.Optional;

public interface BookingClassPort {

    Optional<GymClass> findById(Long classId);

    Optional<GymClass> findByIdForUpdate(Long classId);

    void updateAvailableSpots(GymClass gymClass);
}
