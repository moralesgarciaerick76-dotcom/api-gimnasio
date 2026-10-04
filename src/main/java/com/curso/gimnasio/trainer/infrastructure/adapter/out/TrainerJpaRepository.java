package com.curso.gimnasio.trainer.infrastructure.adapter.out;

import com.curso.gimnasio.trainer.infrastructure.entities.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerJpaRepository extends JpaRepository<TrainerEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);
}
