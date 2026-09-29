package com.curso.gimnasio.member.domain.exception;

public class MemberBusinessRuleException extends RuntimeException {

    public MemberBusinessRuleException(String message) {
        super(message);
    }
}
