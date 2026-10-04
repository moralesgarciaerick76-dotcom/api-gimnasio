package com.curso.gimnasio.booking.infrastructure.adapter.out;

import com.curso.gimnasio.booking.application.port.out.BookingClassPort;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.gymclass.domain.model.GymClass;
import com.curso.gimnasio.gymclass.infrastructure.adapter.out.GymClassJpaRepository;
import com.curso.gimnasio.gymclass.infrastructure.adapter.out.GymClassPersistenceMapper;
import com.curso.gimnasio.gymclass.infrastructure.entities.GymClassEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookingClassAdapter implements BookingClassPort {

    private final GymClassJpaRepository gymClassJpaRepository;

    public BookingClassAdapter(GymClassJpaRepository gymClassJpaRepository) {
        this.gymClassJpaRepository = gymClassJpaRepository;
    }

    @Override
    public Optional<GymClass> findById(Long classId) {
        return gymClassJpaRepository.findById(classId).map(GymClassPersistenceMapper::toDomain);
    }

    @Override
    public Optional<GymClass> findByIdForUpdate(Long classId) {
        return gymClassJpaRepository.findByIdForUpdate(classId).map(GymClassPersistenceMapper::toDomain);
    }

    @Override
    public void updateAvailableSpots(GymClass gymClass) {
        GymClassEntity entity = gymClassJpaRepository.findById(gymClass.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada: " + gymClass.getId()));
        entity.setAvailableSpots(gymClass.getAvailableSpots());
        gymClassJpaRepository.save(entity);
    }
}
