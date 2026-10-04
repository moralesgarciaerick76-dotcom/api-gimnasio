package com.curso.gimnasio.trainer.application.port.in;

import com.curso.gimnasio.trainer.domain.model.Trainer;

import java.util.List;

public interface GetTrainerUseCase {

    Trainer findById(Long id);

    List<Trainer> findAll();
}
