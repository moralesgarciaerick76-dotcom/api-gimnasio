package com.curso.gimnasio.gymclass.application.service;

import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.gymclass.application.port.in.CreateGymClassCommand;
import com.curso.gimnasio.gymclass.application.port.in.CreateGymClassUseCase;
import com.curso.gimnasio.gymclass.application.port.in.DeleteGymClassUseCase;
import com.curso.gimnasio.gymclass.application.port.in.GetGymClassUseCase;
import com.curso.gimnasio.gymclass.application.port.out.GymClassBookingsPort;
import com.curso.gimnasio.gymclass.application.port.out.GymClassRepositoryPort;
import com.curso.gimnasio.gymclass.application.port.out.GymClassTrainerPort;
import com.curso.gimnasio.gymclass.domain.model.GymClass;
import com.curso.gimnasio.trainer.domain.model.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GymClassService implements CreateGymClassUseCase, GetGymClassUseCase, DeleteGymClassUseCase {

    private final GymClassRepositoryPort repository;
    private final GymClassTrainerPort trainerPort;
    private final GymClassBookingsPort bookingsPort;

    public GymClassService(GymClassRepositoryPort repository,
                           GymClassTrainerPort trainerPort,
                           GymClassBookingsPort bookingsPort) {
        this.repository = repository;
        this.trainerPort = trainerPort;
        this.bookingsPort = bookingsPort;
    }

    @Override
    @Transactional
    public GymClass create(CreateGymClassCommand command) {
        String name = command.getName().trim();
        if (repository.existsByName(name)) {
            throw new BusinessRuleException("Ya existe una clase con el nombre: " + name);
        }
        Trainer trainer = trainerPort.findById(command.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado: " + command.getTrainerId()));

        GymClass gymClass = new GymClass(null, name, command.getSchedule().trim(), toMoney(command.getPrice()),
                command.getAvailableSpots(), trainer.getId(), trainer.getName());

        return repository.save(gymClass);
    }

    @Override
    public GymClass findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada: " + id));
    }

    @Override
    public List<GymClass> findAll() {
        return repository.findAll();
    }

    @Override
    public List<GymClass> searchByName(String name) {
        return repository.searchByName(name.trim());
    }

    @Override
    public List<GymClass> findBySchedule(String schedule) {
        return repository.findBySchedule(schedule.trim());
    }

    @Override
    public List<GymClass> filterByPriceAndSpots(BigDecimal maxPrice, Integer minSpots) {
        return repository.findByMaxPriceAndMinSpots(maxPrice, minSpots);
    }

    @Override
    public List<GymClass> findAvailable() {
        return repository.findWithSpots();
    }

    @Override
    public List<GymClass> findByTrainerName(String trainerName) {
        return repository.findByTrainerName(trainerName.trim());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        GymClass gymClass = findById(id);
        if (bookingsPort.isInBookings(id)) {
            throw new BusinessRuleException("No se puede eliminar la clase '" + gymClass.getName()
                    + "' porque aparece en reservas registradas");
        }
        repository.delete(gymClass);
    }

    private BigDecimal toMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
