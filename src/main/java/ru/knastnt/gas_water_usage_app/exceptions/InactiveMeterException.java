package ru.knastnt.gas_water_usage_app.exceptions;

public class InactiveMeterException extends RuntimeException{
    public InactiveMeterException(String message) {
        super(message);
    }
}
