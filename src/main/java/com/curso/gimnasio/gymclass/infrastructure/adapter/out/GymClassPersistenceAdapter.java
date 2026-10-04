package com.curso.gimnasio.gymclass.infrastructure.adapter.out;

import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.gymclass.application.port.out.GymClassRepositoryPort;
import com.curso.gimnasio.gymclass.domain.model.GymClass;
import com.curso.gimnasio.gymclass.infrastructure.entities.GymClassEntity;
import com.curso.gimnasio.trainer.infrastructure.adapter.out.TrainerJpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class GymClassPersistenceAdapter implements GymClassRepositoryPort {

    private final GymClassJpaRepository gymClassJpaRepository;
    private final TrainerJpaRepository trainerJpaRepository;

    public GymClassPersistenceAdapter(GymClassJpaRepository gymClassJpaRepository,
                                      TrainerJpaRepository trainerJpaRepository) {
        this.gymClassJpaRepository = gymClassJpaRepository;
        this.trainerJpaRepository = trainerJpaRepository;
    }

    @Override
    public GymClass save(GymClass gymClass) {
        GymClassEntity entity;
        if (gymClass.getId() == null) {
            entity = new GymClassEntity(gymClass.getName(), gymClass.getSchedule(), gymClass.getPrice(),
                    gymClass.getAvailableSpots(), trainerJpaRepository.getReferenceById(gymClass.getTrainerId()));
        } else {
            entity = gymClassJpaRepository.findById(gymClass.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada: " + gymClass.getId()));
            entity.setName(gymClass.getName());
            entity.setSchedule(gymClass.getSchedule());
            entity.setPrice(gymClass.getPrice());
            entity.setAvailableSpots(gymClass.getAvailableSpots());
        }
        return GymClassPersistenceMapper.toDomain(gymClassJpaRepository.save(entity));
    }

    @Override
    public List<GymClass> findAll() {
        return toDomainList(gymClassJpaRepository.findAll());
    }

    @Override
    public Optional<GymClass> findById(Long id) {
        return gymClassJpaRepository.findById(id).map(GymClassPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return gymClassJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void delete(GymClass gymClass) {
        gymClassJpaRepository.deleteById(gymClass.getId());
    }

    @Override
    public List<GymClass> searchByName(String name) {
        return toDomainList(gymClassJpaRepository.findByNameContainingIgnoreCase(name));
    }

    @Override
    public List<GymClass> findBySchedule(String schedule) {
        return toDomainList(gymClassJpaRepository.findByScheduleContainingIgnoreCase(schedule));
    }

    @Override
    public List<GymClass> findByMaxPriceAndMinSpots(BigDecimal maxPrice, Integer minSpots) {
        return toDomainList(gymClassJpaRepository.findByPriceLessThanEqualAndAvailableSpotsGreaterThanEqual(maxPrice, minSpots));
    }

    @Override
    public List<GymClass> findWithSpots() {
        return toDomainList(gymClassJpaRepository.findClassesWithSpots());
    }

    @Override
    public List<GymClass> findByTrainerName(String trainerName) {
        return toDomainList(gymClassJpaRepository.findByTrainerNameIgnoreCase(trainerName));
    }

    private List<GymClass> toDomainList(List<GymClassEntity> entities) {
        return entities.stream()
                .map(GymClassPersistenceMapper::toDomain)
                .toList();
    }
}
