package ru.anokhin.dev.metrologyapp.exception.employee;

public class EmployeeEmailAlreadyExistException extends RuntimeException {
    public EmployeeEmailAlreadyExistException(String message) {
        super(message);
    }
}
