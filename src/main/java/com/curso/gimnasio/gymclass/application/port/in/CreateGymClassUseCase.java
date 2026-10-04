package com.curso.gimnasio.gymclass.application.port.in;

import com.curso.gimnasio.gymclass.domain.model.GymClass;

public interface CreateGymClassUseCase {

    GymClass create(CreateGymClassCommand command);
}
