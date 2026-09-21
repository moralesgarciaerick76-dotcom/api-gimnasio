package com.curso.gimnasio.repository;

import com.curso.gimnasio.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    boolean existsByEmailIgnoreCase(String email);
}
