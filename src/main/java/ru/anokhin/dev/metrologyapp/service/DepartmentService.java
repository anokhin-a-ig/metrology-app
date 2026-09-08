package ru.anokhin.dev.metrologyapp.service;

import org.springframework.stereotype.Service;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.mapper.DepartmentMapper;
import ru.anokhin.dev.metrologyapp.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.List;

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

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Департамент с id: " + id + " не найден"));
    }

    public List<DepartmentResponseDto> findAll() {
        List<Department> deps = departmentRepository.findAll();
        List<DepartmentResponseDto> depsDto = new ArrayList<>();
        for(Department d : deps) {
            depsDto.add(departmentMapper.toDto(d));
        }
        return depsDto;
    }
}