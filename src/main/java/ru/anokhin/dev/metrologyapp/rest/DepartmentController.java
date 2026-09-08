package ru.anokhin.dev.metrologyapp.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.service.DepartmentDtoService;
import ru.anokhin.dev.metrologyapp.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentDtoService departmentDtoService;

    public DepartmentController(DepartmentService departmentService, DepartmentDtoService departmentDtoService) {
        this.departmentService = departmentService;
        this.departmentDtoService = departmentDtoService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> create(@Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto created = departmentDtoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> findAll() {
        //TODO переделать с departmentService на departmentDtoService
        List<DepartmentResponseDto> depList = departmentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(depList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                departmentDtoService.findById(id)
        );
    }
}