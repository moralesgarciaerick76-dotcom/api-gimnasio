package com.curso.gimnasio.config;

import com.curso.gimnasio.gymclass.application.port.in.CreateGymClassCommand;
import com.curso.gimnasio.gymclass.application.port.in.CreateGymClassUseCase;
import com.curso.gimnasio.member.application.port.in.CreateMemberCommand;
import com.curso.gimnasio.member.application.port.in.CreateMemberUseCase;
import com.curso.gimnasio.trainer.application.port.in.CreateTrainerCommand;
import com.curso.gimnasio.trainer.application.port.in.CreateTrainerUseCase;
import com.curso.gimnasio.trainer.application.port.in.GetTrainerUseCase;
import com.curso.gimnasio.trainer.domain.model.Trainer;
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

    private final CreateTrainerUseCase createTrainerUseCase;
    private final GetTrainerUseCase getTrainerUseCase;
    private final CreateGymClassUseCase createGymClassUseCase;
    private final CreateMemberUseCase createMemberUseCase;

    public DataInitializer(CreateTrainerUseCase createTrainerUseCase,
                           GetTrainerUseCase getTrainerUseCase,
                           CreateGymClassUseCase createGymClassUseCase,
                           CreateMemberUseCase createMemberUseCase) {
        this.createTrainerUseCase = createTrainerUseCase;
        this.getTrainerUseCase = getTrainerUseCase;
        this.createGymClassUseCase = createGymClassUseCase;
        this.createMemberUseCase = createMemberUseCase;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!getTrainerUseCase.findAll().isEmpty()) {
            return;
        }

        Trainer lucia = createTrainerUseCase.create(new CreateTrainerCommand("Lucía Fernández", "lucia.fernandez@gym.com", "Yoga"));
        Trainer marco = createTrainerUseCase.create(new CreateTrainerCommand("Marco Rivas", "marco.rivas@gym.com", "Spinning"));
        Trainer andrea = createTrainerUseCase.create(new CreateTrainerCommand("Andrea Castillo", "andrea.castillo@gym.com", "Funcional"));

        // "Spinning Principiantes" arranca sin cupos para poder probar el caso de error.
        createGymClassUseCase.create(new CreateGymClassCommand("Yoga Matutino", "Lun-Mié-Vie 07:00", new BigDecimal("25.00"), 12, lucia.getId()));
        createGymClassUseCase.create(new CreateGymClassCommand("Yoga Restaurativo", "Sáb 09:00", new BigDecimal("30.00"), 8, lucia.getId()));
        createGymClassUseCase.create(new CreateGymClassCommand("Spinning Intenso", "Mar-Jue 19:00", new BigDecimal("20.00"), 15, marco.getId()));
        createGymClassUseCase.create(new CreateGymClassCommand("Spinning Principiantes", "Lun-Mié 18:00", new BigDecimal("18.00"), 0, marco.getId()));
        createGymClassUseCase.create(new CreateGymClassCommand("Funcional Nocturno", "Lun-Mié-Vie 20:00", new BigDecimal("22.00"), 10, andrea.getId()));

        createMemberUseCase.create(new CreateMemberCommand("Carla Mendoza", "carla.mendoza@mail.com"));
        createMemberUseCase.create(new CreateMemberCommand("Diego Salazar", "diego.salazar@mail.com"));
        createMemberUseCase.create(new CreateMemberCommand("María Quispe", "maria.quispe@mail.com"));

        log.info("Datos de ejemplo cargados: 3 entrenadores, 5 clases y 3 socios.");
    }
}
