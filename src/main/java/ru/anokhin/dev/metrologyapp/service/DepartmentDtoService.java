package ru.anokhin.dev.metrologyapp.service;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.mapper.DepartmentMapper;

import java.util.List;

@Service
public class DepartmentDtoService {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    DepartmentDtoService(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    public DepartmentResponseDto save(DepartmentRequestDto dto) {
        Department parent = null;
        if (dto.parentId() != null) {
            parent = departmentService.findById(dto.parentId());
        }

        Department department = departmentMapper.toEntity(dto, parent);
        return departmentMapper.toDto(departmentService.save(department));
    }

    public DepartmentResponseDto findById(Long id) {
        return departmentMapper.toDto(departmentService.findById(id));
    }

    public List<DepartmentResponseDto> findAll() {
        return departmentService.findAll().stream()
                .map(departmentMapper::toDtoWithoutChildren).toList();

    }

    public DepartmentResponseDto changeById(Long id, @Valid DepartmentRequestDto dto) {
        Department department = departmentService.findById(id);
        if (department != null) {
            department.setId(id);
            department.setName(dto.name());
            department.setType(dto.type());
            department.setParent(dto.parentId() != null ? departmentService.findById(dto.parentId()) : null);
            departmentService.save(department);
            return departmentMapper.toDto(department);
        } else {
            throw new IllegalArgumentException("Департамент с id: " + id + " не найден");
        }
    }
}
