package com.curso.gimnasio.gymclass.application.port.out;

import com.curso.gimnasio.gymclass.domain.model.GymClass;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GymClassRepositoryPort {

    GymClass save(GymClass gymClass);

    List<GymClass> findAll();

    Optional<GymClass> findById(Long id);

    boolean existsByName(String name);

    void delete(GymClass gymClass);

    List<GymClass> searchByName(String name);

    List<GymClass> findBySchedule(String schedule);

    List<GymClass> findByMaxPriceAndMinSpots(BigDecimal maxPrice, Integer minSpots);

    List<GymClass> findWithSpots();

    List<GymClass> findByTrainerName(String trainerName);
}
