package com.curso.gimnasio.trainer.infrastructure.adapter.out;

import com.curso.gimnasio.trainer.domain.model.Trainer;
import com.curso.gimnasio.trainer.infrastructure.entities.TrainerEntity;

public class TrainerPersistenceMapper {

    private TrainerPersistenceMapper() {
    }

    public static TrainerEntity toEntity(Trainer trainer) {
        TrainerEntity entity = new TrainerEntity(trainer.getName(), trainer.getEmail(), trainer.getSpecialty());
        entity.setId(trainer.getId());
        return entity;
    }

    public static Trainer toDomain(TrainerEntity entity) {
        return new Trainer(entity.getId(), entity.getName(), entity.getEmail(), entity.getSpecialty());
    }
}
