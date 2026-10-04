package com.curso.gimnasio.trainer.infrastructure.adapter.out;

import com.curso.gimnasio.gymclass.infrastructure.adapter.out.GymClassJpaRepository;
import com.curso.gimnasio.trainer.application.port.out.TrainerClassesPort;
import org.springframework.stereotype.Component;

@Component
public class TrainerClassesAdapter implements TrainerClassesPort {

    private final GymClassJpaRepository gymClassJpaRepository;

    public TrainerClassesAdapter(GymClassJpaRepository gymClassJpaRepository) {
        this.gymClassJpaRepository = gymClassJpaRepository;
    }

    @Override
    public boolean hasClasses(Long trainerId) {
        return gymClassJpaRepository.existsByTrainerId(trainerId);
    }
}
