package com.curso.gimnasio.member.domain.exception;

import com.curso.gimnasio.exception.BusinessRuleException;

public class MemberBusinessRuleException extends BusinessRuleException {

    public MemberBusinessRuleException(String message) {
        super(message);
    }
}
