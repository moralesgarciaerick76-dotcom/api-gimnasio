package com.curso.gimnasio.trainer.application.port.out;

import com.curso.gimnasio.trainer.domain.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepositoryPort {

    Trainer save(Trainer trainer);

    List<Trainer> findAll();

    Optional<Trainer> findById(Long id);

    boolean existsByEmail(String email);

    void delete(Trainer trainer);
}
