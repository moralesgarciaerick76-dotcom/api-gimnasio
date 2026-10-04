package com.curso.gimnasio.gymclass.infrastructure.adapter.out;

import com.curso.gimnasio.gymclass.infrastructure.entities.GymClassEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GymClassJpaRepository extends JpaRepository<GymClassEntity, Long> {

    List<GymClassEntity> findByNameContainingIgnoreCase(String name);

    List<GymClassEntity> findByScheduleContainingIgnoreCase(String schedule);

    List<GymClassEntity> findByPriceLessThanEqualAndAvailableSpotsGreaterThanEqual(BigDecimal maxPrice, Integer minSpots);

    List<GymClassEntity> findByTrainerNameIgnoreCase(String trainerName);

    @Query("SELECT c FROM GymClass c WHERE c.availableSpots > 0 ORDER BY c.name")
    List<GymClassEntity> findClassesWithSpots();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM GymClass c WHERE c.id = :id")
    Optional<GymClassEntity> findByIdForUpdate(@Param("id") Long id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByTrainerId(Long trainerId);
}
