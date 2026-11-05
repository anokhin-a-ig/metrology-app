package ru.anokhin.dev.metrologyapp.exception.employee;

public class EmployeeFactoryNumberAlreadyExistException extends RuntimeException {
    public EmployeeFactoryNumberAlreadyExistException(String message) {
        super(message);
    }
}
