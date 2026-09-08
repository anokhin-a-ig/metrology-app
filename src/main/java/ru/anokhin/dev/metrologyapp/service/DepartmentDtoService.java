package ru.anokhin.dev.metrologyapp.service;

import org.springframework.stereotype.Service;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.mapper.DepartmentMapper;

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
}
