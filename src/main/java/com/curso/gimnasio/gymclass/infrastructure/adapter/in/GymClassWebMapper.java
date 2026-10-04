package com.curso.gimnasio.gymclass.infrastructure.adapter.in;

import com.curso.gimnasio.gymclass.application.port.in.CreateGymClassCommand;
import com.curso.gimnasio.gymclass.domain.model.GymClass;

import java.util.List;

public class GymClassWebMapper {

    private GymClassWebMapper() {
    }

    public static GymClassResponse toResponse(GymClass gymClass) {
        return new GymClassResponse(
                gymClass.getId(),
                gymClass.getName(),
                gymClass.getSchedule(),
                gymClass.getPrice(),
                gymClass.getAvailableSpots(),
                gymClass.getTrainerId(),
                gymClass.getTrainerName()
        );
    }

    public static List<GymClassResponse> toResponseList(List<GymClass> classes) {
        return classes.stream()
                .map(GymClassWebMapper::toResponse)
                .toList();
    }

    public static CreateGymClassCommand toCommand(GymClassRequest request) {
        return new CreateGymClassCommand(request.getName(), request.getSchedule(), request.getPrice(),
                request.getAvailableSpots(), request.getTrainerId());
    }
}
