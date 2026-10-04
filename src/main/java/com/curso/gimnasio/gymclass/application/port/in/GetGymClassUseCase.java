package com.curso.gimnasio.gymclass.application.port.in;

import com.curso.gimnasio.gymclass.domain.model.GymClass;

import java.math.BigDecimal;
import java.util.List;

public interface GetGymClassUseCase {

    GymClass findById(Long id);

    List<GymClass> findAll();

    List<GymClass> searchByName(String name);

    List<GymClass> findBySchedule(String schedule);

    List<GymClass> filterByPriceAndSpots(BigDecimal maxPrice, Integer minSpots);

    List<GymClass> findAvailable();

    List<GymClass> findByTrainerName(String trainerName);
}
