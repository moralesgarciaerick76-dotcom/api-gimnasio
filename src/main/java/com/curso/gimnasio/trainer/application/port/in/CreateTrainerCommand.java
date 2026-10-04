package com.curso.gimnasio.trainer.application.port.in;

public class CreateTrainerCommand {

    private String name;
    private String email;
    private String specialty;

    public CreateTrainerCommand(String name, String email, String specialty) {
        this.name = name;
        this.email = email;
        this.specialty = specialty;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialty() {
        return specialty;
    }
}
