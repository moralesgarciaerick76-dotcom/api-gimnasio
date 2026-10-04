package com.curso.gimnasio.trainer.application.port.in;

import com.curso.gimnasio.trainer.domain.model.Trainer;

public interface CreateTrainerUseCase {

    Trainer create(CreateTrainerCommand command);
}
