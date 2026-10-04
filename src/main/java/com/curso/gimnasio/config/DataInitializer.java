package com.curso.gimnasio.config;

import com.curso.gimnasio.entity.GymClass;
import com.curso.gimnasio.member.application.port.in.CreateMemberCommand;
import com.curso.gimnasio.member.application.port.in.CreateMemberUseCase;
import com.curso.gimnasio.repository.GymClassRepository;
import com.curso.gimnasio.trainer.infrastructure.adapter.out.TrainerJpaRepository;
import com.curso.gimnasio.trainer.infrastructure.entities.TrainerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Carga datos de ejemplo al arrancar (solo si la base está vacía), para poder
 * probar los endpoints sin tener que crear todo a mano primero.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final TrainerJpaRepository trainerRepository;
    private final GymClassRepository gymClassRepository;
    private final CreateMemberUseCase createMemberUseCase;

    public DataInitializer(TrainerJpaRepository trainerRepository,
                           GymClassRepository gymClassRepository,
                           CreateMemberUseCase createMemberUseCase) {
        this.trainerRepository = trainerRepository;
        this.gymClassRepository = gymClassRepository;
        this.createMemberUseCase = createMemberUseCase;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (trainerRepository.count() > 0) {
            return;
        }

        TrainerEntity lucia = trainerRepository.save(new TrainerEntity("Lucía Fernández", "lucia.fernandez@gym.com", "Yoga"));
        TrainerEntity marco = trainerRepository.save(new TrainerEntity("Marco Rivas", "marco.rivas@gym.com", "Spinning"));
        TrainerEntity andrea = trainerRepository.save(new TrainerEntity("Andrea Castillo", "andrea.castillo@gym.com", "Funcional"));

        // "Spinning Principiantes" arranca sin cupos para poder probar el caso de error.
        gymClassRepository.save(new GymClass("Yoga Matutino", "Lun-Mié-Vie 07:00", new BigDecimal("25.00"), 12, lucia));
        gymClassRepository.save(new GymClass("Yoga Restaurativo", "Sáb 09:00", new BigDecimal("30.00"), 8, lucia));
        gymClassRepository.save(new GymClass("Spinning Intenso", "Mar-Jue 19:00", new BigDecimal("20.00"), 15, marco));
        gymClassRepository.save(new GymClass("Spinning Principiantes", "Lun-Mié 18:00", new BigDecimal("18.00"), 0, marco));
        gymClassRepository.save(new GymClass("Funcional Nocturno", "Lun-Mié-Vie 20:00", new BigDecimal("22.00"), 10, andrea));

        createMemberUseCase.create(new CreateMemberCommand("Carla Mendoza", "carla.mendoza@mail.com"));
        createMemberUseCase.create(new CreateMemberCommand("Diego Salazar", "diego.salazar@mail.com"));
        createMemberUseCase.create(new CreateMemberCommand("María Quispe", "maria.quispe@mail.com"));

        log.info("Datos de ejemplo cargados: 3 entrenadores, 5 clases y 3 socios.");
    }
}
