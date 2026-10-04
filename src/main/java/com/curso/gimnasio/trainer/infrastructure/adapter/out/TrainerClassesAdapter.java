package com.curso.gimnasio.trainer.infrastructure.adapter.out;

import com.curso.gimnasio.repository.GymClassRepository;
import com.curso.gimnasio.trainer.application.port.out.TrainerClassesPort;
import org.springframework.stereotype.Component;

@Component
public class TrainerClassesAdapter implements TrainerClassesPort {

    private final GymClassRepository gymClassJpaRepository;

    public TrainerClassesAdapter(GymClassRepository gymClassJpaRepository) {
        this.gymClassJpaRepository = gymClassJpaRepository;
    }

    @Override
    public boolean hasClasses(Long trainerId) {
        return gymClassJpaRepository.existsByTrainerId(trainerId);
    }
}
