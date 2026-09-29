package com.curso.gimnasio.member.application.port.in;

public class CreateMemberCommand {

    private String name;
    private String email;

    public CreateMemberCommand(String name, String email) {
        this.name = name;
        this.email = email;
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
}
