package com.curso.gimnasio.service;

import com.curso.gimnasio.dto.TrainerDto;
import com.curso.gimnasio.entity.Trainer;
import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.repository.GymClassRepository;
import com.curso.gimnasio.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final GymClassRepository gymClassRepository;

    public TrainerService(TrainerRepository trainerRepository, GymClassRepository gymClassRepository) {
        this.trainerRepository = trainerRepository;
        this.gymClassRepository = gymClassRepository;
    }

    @Transactional
    public TrainerDto create(TrainerDto request) {
        String email = request.getEmail().trim();
        if (trainerRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessRuleException("Ya existe un entrenador con el email: " + email);
        }
        Trainer saved = trainerRepository.save(
                new Trainer(request.getName().trim(), email, request.getSpecialty().trim()));
        return toDto(saved);
    }

    public List<TrainerDto> findAll() {
        return trainerRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public TrainerDto findById(Long id) {
        return toDto(findTrainer(id));
    }

    @Transactional
    public void delete(Long id) {
        Trainer trainer = findTrainer(id);
        if (gymClassRepository.existsByTrainerId(id)) {
            throw new BusinessRuleException("No se puede eliminar al entrenador '" + trainer.getName()
                    + "' porque tiene clases asignadas");
        }
        trainerRepository.delete(trainer);
    }

    private Trainer findTrainer(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado: " + id));
    }

    private TrainerDto toDto(Trainer trainer) {
        return new TrainerDto(trainer.getId(), trainer.getName(), trainer.getEmail(), trainer.getSpecialty());
    }
}
