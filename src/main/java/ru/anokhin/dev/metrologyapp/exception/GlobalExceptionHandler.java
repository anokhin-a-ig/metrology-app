package ru.anokhin.dev.metrologyapp.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.anokhin.dev.metrologyapp.exception.employee.EmployeeEmailAlreadyExistException;
import ru.anokhin.dev.metrologyapp.exception.employee.EmployeeFactoryNumberAlreadyExistException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return badRequest(ex.getMessage());
    }

    @ExceptionHandler(DuplicateDepartmentException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateDepartment(DuplicateDepartmentException ex) {
        return badRequest(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Некорректное тело запроса");
        return badRequest(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = isInvalidDepartmentType(ex)
                ? "Некорректный тип департамента"
                : "Некорректное тело запроса";
        return badRequest(message);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentNotFound(DepartmentNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, 404, ex.getMessage());
    }

    @ExceptionHandler(ArchiveDepartmentException.class)
    public ResponseEntity<Map<String, Object>> handleArchiveDepartment(ArchiveDepartmentException ex) {
        return buildResponse(HttpStatus.CONFLICT, 409, ex.getMessage());
    }

    @ExceptionHandler(DepartmentCycleException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentCycle(DepartmentCycleException ex) {
        return buildResponse(HttpStatus.CONFLICT, 409, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String cause = ex.getMostSpecificCause().getMessage();
        String message = cause != null
                && (cause.contains("ux_department_name_lower_icu") || cause.contains("uc_department_name"))
                ? "Департамент с таким названием уже существует"
                : "Не удалось сохранить департамент";

        return badRequest(message);
    }

    private boolean isInvalidDepartmentType(HttpMessageNotReadableException ex) {
        return ex.getCause() instanceof InvalidFormatException invalidFormatException
                && invalidFormatException.getPath().stream()
                .anyMatch(reference -> "type".equals(reference.getFieldName()));
    }

    private ResponseEntity<Map<String, Object>> badRequest(String message) {
        return buildResponse(HttpStatus.BAD_REQUEST, 400, message);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, int statusCode, String message) {
        return ResponseEntity
                .status(status)
                .body(Map.of(
                        "message", message,
                        "status", statusCode,
                        "timestamp", LocalDateTime.now()
                ));
    }


    @ExceptionHandler(EmployeeEmailAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeEmailAlreadyExist (EmployeeEmailAlreadyExistException ex) {
        return buildResponse(HttpStatus.CONFLICT, 409, ex.getMessage());
    }

    @ExceptionHandler(EmployeeFactoryNumberAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeFactoryNumberAlreadyExist(EmployeeFactoryNumberAlreadyExistException ex) {
        return buildResponse(HttpStatus.CONFLICT, 409, ex.getMessage());
    }
}
