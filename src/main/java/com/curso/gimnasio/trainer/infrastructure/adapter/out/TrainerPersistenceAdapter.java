package com.curso.gimnasio.trainer.infrastructure.adapter.out;

import com.curso.gimnasio.trainer.application.port.out.TrainerRepositoryPort;
import com.curso.gimnasio.trainer.domain.model.Trainer;
import com.curso.gimnasio.trainer.infrastructure.entities.TrainerEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrainerPersistenceAdapter implements TrainerRepositoryPort {

    private final TrainerJpaRepository trainerJpaRepository;

    public TrainerPersistenceAdapter(TrainerJpaRepository trainerJpaRepository) {
        this.trainerJpaRepository = trainerJpaRepository;
    }

    @Override
    public Trainer save(Trainer trainer) {
        TrainerEntity saved = trainerJpaRepository.save(TrainerPersistenceMapper.toEntity(trainer));
        return TrainerPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerJpaRepository.findAll().stream()
                .map(TrainerPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return trainerJpaRepository.findById(id).map(TrainerPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return trainerJpaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public void delete(Trainer trainer) {
        trainerJpaRepository.deleteById(trainer.getId());
    }
}
