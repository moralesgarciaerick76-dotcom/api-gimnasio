package com.curso.gimnasio.trainer.infrastructure.adapter.in;

import com.curso.gimnasio.trainer.application.port.in.CreateTrainerCommand;
import com.curso.gimnasio.trainer.domain.model.Trainer;

import java.util.List;

public class TrainerWebMapper {

    private TrainerWebMapper() {
    }

    public static TrainerDto toDto(Trainer trainer) {
        return new TrainerDto(trainer.getId(), trainer.getName(), trainer.getEmail(), trainer.getSpecialty());
    }

    public static List<TrainerDto> toDtoList(List<Trainer> trainers) {
        return trainers.stream()
                .map(TrainerWebMapper::toDto)
                .toList();
    }

    public static CreateTrainerCommand toCommand(TrainerDto request) {
        return new CreateTrainerCommand(request.getName(), request.getEmail(), request.getSpecialty());
    }
}
