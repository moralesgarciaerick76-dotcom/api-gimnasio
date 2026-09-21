package com.curso.gimnasio.exception;

/**
 * La petición está bien formada pero choca con una regla de negocio o con el estado
 * actual de los datos (sin cupos, email repetido, reserva ya cancelada...).
 * Se traduce a HTTP 409 Conflict.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
