package com.curso.gimnasio.service;

import com.curso.gimnasio.dto.GymClassRequest;
import com.curso.gimnasio.dto.GymClassResponse;
import com.curso.gimnasio.entity.GymClass;
import com.curso.gimnasio.entity.Trainer;
import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.repository.BookingRepository;
import com.curso.gimnasio.repository.GymClassRepository;
import com.curso.gimnasio.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GymClassService {

    private final GymClassRepository gymClassRepository;
    private final TrainerRepository trainerRepository;
    private final BookingRepository bookingRepository;

    public GymClassService(GymClassRepository gymClassRepository,
                           TrainerRepository trainerRepository,
                           BookingRepository bookingRepository) {
        this.gymClassRepository = gymClassRepository;
        this.trainerRepository = trainerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public GymClassResponse create(GymClassRequest request) {
        String name = request.getName().trim();
        if (gymClassRepository.existsByNameIgnoreCase(name)) {
            throw new BusinessRuleException("Ya existe una clase con el nombre: " + name);
        }
        Trainer trainer = findTrainer(request.getTrainerId());

        GymClass gymClass = new GymClass(name, request.getSchedule().trim(), toMoney(request.getPrice()),
                request.getAvailableSpots(), trainer);
        return toResponse(gymClassRepository.save(gymClass));
    }

    public List<GymClassResponse> findAll() {
        return toResponseList(gymClassRepository.findAll());
    }

    public GymClassResponse findById(Long id) {
        return toResponse(findGymClass(id));
    }

    @Transactional
    public void delete(Long id) {
        GymClass gymClass = findGymClass(id);
        if (bookingRepository.existsByItemsGymClassId(id)) {
            throw new BusinessRuleException("No se puede eliminar la clase '" + gymClass.getName()
                    + "' porque aparece en reservas registradas");
        }
        gymClassRepository.delete(gymClass);
    }

    // --- búsquedas ---

    public List<GymClassResponse> searchByName(String name) {
        return toResponseList(gymClassRepository.findByNameContainingIgnoreCase(name.trim()));
    }

    public List<GymClassResponse> findBySchedule(String schedule) {
        return toResponseList(gymClassRepository.findByScheduleContainingIgnoreCase(schedule.trim()));
    }

    public List<GymClassResponse> filterByPriceAndSpots(BigDecimal maxPrice, Integer minSpots) {
        return toResponseList(
                gymClassRepository.findByPriceLessThanEqualAndAvailableSpotsGreaterThanEqual(maxPrice, minSpots));
    }

    public List<GymClassResponse> findAvailable() {
        return toResponseList(gymClassRepository.findClassesWithSpots());
    }

    public List<GymClassResponse> findByTrainerName(String trainerName) {
        return toResponseList(gymClassRepository.findByTrainerNameIgnoreCase(trainerName.trim()));
    }

    // --- helpers ---

    /** Los precios se guardan siempre con 2 decimales (25 -> 25.00). */
    private BigDecimal toMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private GymClass findGymClass(Long id) {
        return gymClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada: " + id));
    }

    private Trainer findTrainer(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado: " + id));
    }

    private List<GymClassResponse> toResponseList(List<GymClass> classes) {
        return classes.stream()
                .map(this::toResponse)
                .toList();
    }

    private GymClassResponse toResponse(GymClass gymClass) {
        return new GymClassResponse(
                gymClass.getId(),
                gymClass.getName(),
                gymClass.getSchedule(),
                gymClass.getPrice(),
                gymClass.getAvailableSpots(),
                gymClass.getTrainer().getId(),
                gymClass.getTrainer().getName()
        );
    }
}
