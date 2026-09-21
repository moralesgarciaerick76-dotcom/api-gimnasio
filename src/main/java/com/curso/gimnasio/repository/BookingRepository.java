package com.curso.gimnasio.repository;

import com.curso.gimnasio.entity.Booking;
import com.curso.gimnasio.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // propiedad anidada: Booking -> member -> email
    List<Booking> findByMemberEmailIgnoreCase(String email);

    List<Booking> findByStatus(BookingStatus status);

    // JPQL con JOIN pasando por la tabla intermedia: bookings -> booking_items -> gym_classes -> trainers
    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN b.items i " +
           "JOIN i.gymClass c " +
           "WHERE LOWER(c.trainer.name) = LOWER(:trainerName)")
    List<Booking> findBookingsByTrainerName(@Param("trainerName") String trainerName);

    // --- apoyo para las reglas de negocio ---

    long countByMemberIdAndStatus(Long memberId, BookingStatus status);

    boolean existsByMemberId(Long memberId);

    // ¿la clase aparece en alguna reserva? (recorre la tabla intermedia)
    boolean existsByItemsGymClassId(Long gymClassId);
}
