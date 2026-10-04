package com.curso.gimnasio.gymclass.application.service;

import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.gymclass.application.port.in.CreateGymClassCommand;
import com.curso.gimnasio.gymclass.application.port.out.GymClassBookingsPort;
import com.curso.gimnasio.gymclass.application.port.out.GymClassRepositoryPort;
import com.curso.gimnasio.gymclass.application.port.out.GymClassTrainerPort;
import com.curso.gimnasio.gymclass.domain.model.GymClass;
import com.curso.gimnasio.trainer.domain.model.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymClassServiceTest {

    @Mock
    GymClassRepositoryPort repository;

    @Mock
    GymClassTrainerPort trainerPort;

    @Mock
    GymClassBookingsPort bookingsPort;

    @InjectMocks
    GymClassService gymClassService;

    @Test
    void shouldThrowWhenNameAlreadyExists() {
        when(repository.existsByName("Yoga Matutino")).thenReturn(true);

        assertThrows(BusinessRuleException.class,
                () -> gymClassService.create(new CreateGymClassCommand("Yoga Matutino", "Lun 07:00", new BigDecimal("25"), 12, 1L)));

        verify(repository, never()).save(any(GymClass.class));
    }

    @Test
    void shouldThrowWhenTrainerNotExists() {
        when(repository.existsByName("Yoga Matutino")).thenReturn(false);
        when(trainerPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> gymClassService.create(new CreateGymClassCommand("Yoga Matutino", "Lun 07:00", new BigDecimal("25"), 12, 99L)));

        verify(repository, never()).save(any(GymClass.class));
    }

    @Test
    void shouldCreateClassWithTwoDecimalsAndTrainerName() {
        when(repository.existsByName("Yoga Matutino")).thenReturn(false);
        when(trainerPort.findById(1L)).thenReturn(Optional.of(new Trainer(1L, "Lucía Fernández", "lucia@gym.com", "Yoga")));
        when(repository.save(any(GymClass.class))).thenAnswer(inv -> inv.getArgument(0));

        gymClassService.create(new CreateGymClassCommand("  Yoga Matutino ", " Lun 07:00 ", new BigDecimal("25"), 12, 1L));

        ArgumentCaptor<GymClass> captor = ArgumentCaptor.forClass(GymClass.class);
        verify(repository).save(captor.capture());
        assertEquals("Yoga Matutino", captor.getValue().getName());
        assertEquals("Lun 07:00", captor.getValue().getSchedule());
        assertEquals(new BigDecimal("25.00"), captor.getValue().getPrice());
        assertEquals("Lucía Fernández", captor.getValue().getTrainerName());
    }

    @Test
    void shouldNotDeleteClassUsedInBookings() {
        GymClass yoga = new GymClass(1L, "Yoga Matutino", "Lun 07:00", new BigDecimal("25.00"), 12, 1L, "Lucía Fernández");
        when(repository.findById(1L)).thenReturn(Optional.of(yoga));
        when(bookingsPort.isInBookings(1L)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> gymClassService.delete(1L));

        verify(repository, never()).delete(any(GymClass.class));
    }

    @Test
    void shouldDeleteClassWithoutBookings() {
        GymClass yoga = new GymClass(1L, "Yoga Matutino", "Lun 07:00", new BigDecimal("25.00"), 12, 1L, "Lucía Fernández");
        when(repository.findById(1L)).thenReturn(Optional.of(yoga));
        when(bookingsPort.isInBookings(1L)).thenReturn(false);

        gymClassService.delete(1L);

        verify(repository).delete(yoga);
    }
}
