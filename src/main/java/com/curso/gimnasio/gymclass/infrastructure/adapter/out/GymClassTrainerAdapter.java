package com.curso.gimnasio.gymclass.infrastructure.adapter.out;

import com.curso.gimnasio.gymclass.application.port.out.GymClassTrainerPort;
import com.curso.gimnasio.trainer.domain.model.Trainer;
import com.curso.gimnasio.trainer.infrastructure.adapter.out.TrainerJpaRepository;
import com.curso.gimnasio.trainer.infrastructure.adapter.out.TrainerPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GymClassTrainerAdapter implements GymClassTrainerPort {

    private final TrainerJpaRepository trainerJpaRepository;

    public GymClassTrainerAdapter(TrainerJpaRepository trainerJpaRepository) {
        this.trainerJpaRepository = trainerJpaRepository;
    }

    @Override
    public Optional<Trainer> findById(Long trainerId) {
        return trainerJpaRepository.findById(trainerId).map(TrainerPersistenceMapper::toDomain);
    }
}
