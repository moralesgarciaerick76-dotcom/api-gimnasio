package com.curso.gimnasio.gymclass.infrastructure.adapter.out;

import com.curso.gimnasio.gymclass.domain.model.GymClass;
import com.curso.gimnasio.gymclass.infrastructure.entities.GymClassEntity;

public class GymClassPersistenceMapper {

    private GymClassPersistenceMapper() {
    }

    public static GymClass toDomain(GymClassEntity entity) {
        return new GymClass(
                entity.getId(),
                entity.getName(),
                entity.getSchedule(),
                entity.getPrice(),
                entity.getAvailableSpots(),
                entity.getTrainer().getId(),
                entity.getTrainer().getName()
        );
    }
}
