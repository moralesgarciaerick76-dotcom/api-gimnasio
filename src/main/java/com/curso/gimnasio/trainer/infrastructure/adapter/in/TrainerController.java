package com.curso.gimnasio.trainer.infrastructure.adapter.in;

import com.curso.gimnasio.trainer.application.port.in.CreateTrainerUseCase;
import com.curso.gimnasio.trainer.application.port.in.DeleteTrainerUseCase;
import com.curso.gimnasio.trainer.application.port.in.GetTrainerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Entrenadores")
public class TrainerController {

    private final CreateTrainerUseCase createTrainerUseCase;
    private final GetTrainerUseCase getTrainerUseCase;
    private final DeleteTrainerUseCase deleteTrainerUseCase;

    public TrainerController(CreateTrainerUseCase createTrainerUseCase,
                             GetTrainerUseCase getTrainerUseCase,
                             DeleteTrainerUseCase deleteTrainerUseCase) {
        this.createTrainerUseCase = createTrainerUseCase;
        this.getTrainerUseCase = getTrainerUseCase;
        this.deleteTrainerUseCase = deleteTrainerUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar entrenador")
    public TrainerDto create(@Valid @RequestBody TrainerDto request) {
        return TrainerWebMapper.toDto(createTrainerUseCase.create(TrainerWebMapper.toCommand(request)));
    }

    @GetMapping
    @Operation(summary = "Listar entrenadores")
    public List<TrainerDto> findAll() {
        return TrainerWebMapper.toDtoList(getTrainerUseCase.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar entrenador por id")
    public TrainerDto findById(@PathVariable Long id) {
        return TrainerWebMapper.toDto(getTrainerUseCase.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar entrenador (solo si no tiene clases asignadas)")
    public void delete(@PathVariable Long id) {
        deleteTrainerUseCase.delete(id);
    }
}
