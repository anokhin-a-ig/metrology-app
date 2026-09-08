package ru.anokhin.dev.metrologyapp.service;

import org.springframework.stereotype.Service;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.repository.DepartmentRepository;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    DepartmentService(DepartmentRepository repo) {
        this.departmentRepository = repo;
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

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}