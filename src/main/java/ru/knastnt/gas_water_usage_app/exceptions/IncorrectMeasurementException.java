package ru.knastnt.gas_water_usage_app.exceptions;

public class IncorrectMeasurementException extends RuntimeException{
    public IncorrectMeasurementException(String message) {
        super(message);
    }
}
