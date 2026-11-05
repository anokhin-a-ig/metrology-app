package ru.anokhin.dev.metrologyapp.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anokhin.dev.metrologyapp.dto.request.EmployeeRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.EmployeeResponseDto;
import ru.anokhin.dev.metrologyapp.service.EmployeeService;
import ru.anokhin.dev.metrologyapp.service.dto.EmployeeDtoService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeDtoService employeeDtoService;

    EmployeeController(EmployeeDtoService service) {
        this.employeeDtoService = service;
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDto> save(@RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto saved = employeeDtoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
