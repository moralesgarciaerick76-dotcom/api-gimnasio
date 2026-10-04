package com.curso.gimnasio.member.domain.exception;

import com.curso.gimnasio.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
