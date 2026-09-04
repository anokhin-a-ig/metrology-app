package ru.anokhin.dev.metrologyapp.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anokhin.dev.metrologyapp.dto.request.EmployeeRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.EmployeeResponseDto;
import ru.anokhin.dev.metrologyapp.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    EmployeeController(EmployeeService service) {
        this.employeeService = service;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> add(@Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto saved = employeeService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
