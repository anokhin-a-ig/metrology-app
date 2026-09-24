package ru.anokhin.dev.metrologyapp.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.service.DepartmentDtoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentDtoService departmentDtoService;

    public DepartmentController(DepartmentDtoService departmentDtoService) {
        this.departmentDtoService = departmentDtoService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> create(@Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto created = departmentDtoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> findAll() {
        List<DepartmentResponseDto> depList = departmentDtoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(depList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                departmentDtoService.findById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> changeById(@PathVariable Long id,
                                                            @Valid @RequestBody DepartmentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(departmentDtoService.changeById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        departmentDtoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("123");
    }
}