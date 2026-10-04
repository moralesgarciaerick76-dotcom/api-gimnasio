package com.curso.gimnasio.booking.infrastructure.adapter.out;

import com.curso.gimnasio.booking.domain.model.BookingStatus;
import com.curso.gimnasio.booking.infrastructure.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findByMemberEmailIgnoreCase(String email);

    List<BookingEntity> findByStatus(BookingStatus status);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN b.items i " +
           "JOIN i.gymClass c " +
           "WHERE LOWER(c.trainer.name) = LOWER(:trainerName)")
    List<BookingEntity> findBookingsByTrainerName(@Param("trainerName") String trainerName);

    long countByMemberIdAndStatus(Long memberId, BookingStatus status);

    boolean existsByMemberId(Long memberId);

    boolean existsByItemsGymClassId(Long gymClassId);
}
