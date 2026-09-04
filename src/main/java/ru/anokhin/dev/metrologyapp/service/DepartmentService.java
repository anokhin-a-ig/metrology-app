package ru.anokhin.dev.metrologyapp.service;

import org.springframework.stereotype.Service;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.mapper.DepartmentMapper;
import ru.anokhin.dev.metrologyapp.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    DepartmentService(DepartmentRepository repo, DepartmentMapper departmentMapper) {
        this.departmentRepository = repo;
        this.departmentMapper = departmentMapper;
    }

    public boolean existDepartment(Long id) {
        return departmentRepository.existsById(id);
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Департамент с id: " + id + " не найден"));
    }

    public DepartmentResponseDto save(DepartmentRequestDto dto) {
        Department parent = null;
        if (dto.parentId() != null) {
            parent = findById(dto.parentId());
        }

        Department department = departmentMapper.toEntity(dto, parent);
        return departmentMapper.toDto(departmentRepository.save(department));
    }
}