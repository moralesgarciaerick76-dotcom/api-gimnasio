package com.curso.gimnasio.gymclass.application.port.out;

import com.curso.gimnasio.trainer.domain.model.Trainer;

import java.util.Optional;

public interface GymClassTrainerPort {

    Optional<Trainer> findById(Long trainerId);
}
