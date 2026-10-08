package ru.anokhin.dev.metrologyapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.repository.DepartmentRepository;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    DepartmentService(DepartmentRepository repo) {
        this.departmentRepository = repo;
    }

    @Transactional(readOnly = true)
    public boolean existDepartment(Long id) {
        return departmentRepository.existsById(id);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Департамент с id: " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Department findByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Департамента с названием " + name + " не найдено"));
    }
}