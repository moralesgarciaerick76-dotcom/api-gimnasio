package com.curso.gimnasio.trainer.application.service;

import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.trainer.application.port.in.CreateTrainerCommand;
import com.curso.gimnasio.trainer.application.port.in.CreateTrainerUseCase;
import com.curso.gimnasio.trainer.application.port.in.DeleteTrainerUseCase;
import com.curso.gimnasio.trainer.application.port.in.GetTrainerUseCase;
import com.curso.gimnasio.trainer.application.port.out.TrainerClassesPort;
import com.curso.gimnasio.trainer.application.port.out.TrainerRepositoryPort;
import com.curso.gimnasio.trainer.domain.model.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrainerService implements CreateTrainerUseCase, GetTrainerUseCase, DeleteTrainerUseCase {

    private final TrainerRepositoryPort repository;
    private final TrainerClassesPort classesPort;

    public TrainerService(TrainerRepositoryPort repository, TrainerClassesPort classesPort) {
        this.repository = repository;
        this.classesPort = classesPort;
    }

    @Override
    @Transactional
    public Trainer create(CreateTrainerCommand command) {
        String email = command.getEmail().trim();
        if (repository.existsByEmail(email)) {
            throw new BusinessRuleException("Ya existe un entrenador con el email: " + email);
        }

        Trainer trainer = new Trainer();
        trainer.setName(command.getName().trim());
        trainer.setEmail(email);
        trainer.setSpecialty(command.getSpecialty().trim());

        return repository.save(trainer);
    }

    @Override
    public Trainer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado: " + id));
    }

    @Override
    public List<Trainer> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Trainer trainer = findById(id);
        if (classesPort.hasClasses(id)) {
            throw new BusinessRuleException("No se puede eliminar al entrenador '" + trainer.getName()
                    + "' porque tiene clases asignadas");
        }
        repository.delete(trainer);
    }
}
