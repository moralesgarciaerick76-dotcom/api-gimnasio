package com.curso.gimnasio.booking.infrastructure.adapter.out;

import com.curso.gimnasio.booking.domain.model.BookingStatus;
import com.curso.gimnasio.booking.infrastructure.entities.BookingEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findByMemberEmailIgnoreCase(String email);

    List<BookingEntity> findByStatus(BookingStatus status);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN b.items i " +
           "JOIN i.gymClass c " +
           "WHERE LOWER(c.trainer.name) = LOWER(:trainerName)")
    List<BookingEntity> findBookingsByTrainerName(@Param("trainerName") String trainerName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<BookingEntity> findByIdForUpdate(@Param("id") Long id);

    long countByMemberIdAndStatus(Long memberId, BookingStatus status);

    boolean existsByMemberId(Long memberId);

    boolean existsByItemsGymClassId(Long gymClassId);
}
