package com.curso.gimnasio.trainer.domain.model;

public class Trainer {

    private Long id;
    private String name;
    private String email;
    private String specialty;

    public Trainer() {
    }

    public Trainer(Long id, String name, String email, String specialty) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialty = specialty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
