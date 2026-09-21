package com.curso.gimnasio.repository;

import com.curso.gimnasio.entity.GymClass;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {

    // --- consultas derivadas del nombre del método ---

    List<GymClass> findByNameContainingIgnoreCase(String name);

    List<GymClass> findByScheduleContainingIgnoreCase(String schedule);

    List<GymClass> findByPriceLessThanEqualAndAvailableSpotsGreaterThanEqual(BigDecimal maxPrice, Integer minSpots);

    // propiedad anidada: GymClass -> trainer -> name
    List<GymClass> findByTrainerNameIgnoreCase(String trainerName);

    // --- consulta escrita a mano en JPQL ---

    @Query("SELECT c FROM GymClass c WHERE c.availableSpots > 0 ORDER BY c.name")
    List<GymClass> findClassesWithSpots();

    // --- apoyo para las reglas de negocio ---

    /**
     * Lee la clase bloqueando su fila (SELECT ... FOR UPDATE) hasta que termine la transacción.
     * Así, si dos reservas piden a la vez el último cupo, la segunda espera y ve los cupos
     * ya actualizados. Necesita una transacción activa (@Transactional en el service).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM GymClass c WHERE c.id = :id")
    Optional<GymClass> findByIdForUpdate(@Param("id") Long id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByTrainerId(Long trainerId);
}
