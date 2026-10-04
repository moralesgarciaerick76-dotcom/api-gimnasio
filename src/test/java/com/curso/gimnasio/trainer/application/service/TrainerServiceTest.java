package com.curso.gimnasio.trainer.application.service;

import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.trainer.application.port.in.CreateTrainerCommand;
import com.curso.gimnasio.trainer.application.port.out.TrainerClassesPort;
import com.curso.gimnasio.trainer.application.port.out.TrainerRepositoryPort;
import com.curso.gimnasio.trainer.domain.model.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    TrainerRepositoryPort repository;

    @Mock
    TrainerClassesPort classesPort;

    @InjectMocks
    TrainerService trainerService;

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        when(repository.existsByEmail("lucia@gym.com")).thenReturn(true);

        assertThrows(BusinessRuleException.class,
                () -> trainerService.create(new CreateTrainerCommand("Lucía", "lucia@gym.com", "Yoga")));

        verify(repository, never()).save(any(Trainer.class));
    }

    @Test
    void shouldTrimFieldsWhenCreating() {
        when(repository.existsByEmail("lucia@gym.com")).thenReturn(false);
        when(repository.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        trainerService.create(new CreateTrainerCommand("  Lucía Fernández ", " lucia@gym.com ", " Yoga "));

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(repository).save(captor.capture());
        assertEquals("Lucía Fernández", captor.getValue().getName());
        assertEquals("lucia@gym.com", captor.getValue().getEmail());
        assertEquals("Yoga", captor.getValue().getSpecialty());
    }

    @Test
    void shouldThrowWhenTrainerNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> trainerService.findById(99L));
    }

    @Test
    void shouldNotDeleteTrainerWithClasses() {
        Trainer lucia = new Trainer(1L, "Lucía Fernández", "lucia@gym.com", "Yoga");
        when(repository.findById(1L)).thenReturn(Optional.of(lucia));
        when(classesPort.hasClasses(1L)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> trainerService.delete(1L));

        verify(repository, never()).delete(any(Trainer.class));
    }

    @Test
    void shouldDeleteTrainerWithoutClasses() {
        Trainer lucia = new Trainer(1L, "Lucía Fernández", "lucia@gym.com", "Yoga");
        when(repository.findById(1L)).thenReturn(Optional.of(lucia));
        when(classesPort.hasClasses(1L)).thenReturn(false);

        trainerService.delete(1L);

        verify(repository).delete(lucia);
    }
}
