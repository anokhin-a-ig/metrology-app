package ru.anokhin.dev.metrologyapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;
import ru.anokhin.dev.metrologyapp.repository.DepartmentRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

    @Transactional(readOnly = true)
    public Page<Department> findAllWithPagination(Integer offset, Integer limit) {
        return departmentRepository.findAll(PageRequest.of(offset, limit));
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Department findByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Департамента с названием " + name + " не найдено"));
    }

    @Transactional(readOnly = true)
    public List<Department> findByType(String type) {
        DepartmentType depType = parseType(type);
        List<Department> departmentList = departmentRepository.findDepartmentByType(depType).orElse(List.of());
        if (departmentList.isEmpty()) {
            throw new IllegalArgumentException("Департаментов с типом " + type + " не найдено");
        }
        return departmentList;
    }

    @Transactional(readOnly = true)
    public Page<Department> findAllWithPageable(Integer page, Integer size, String type, String sortBy) {
        Pageable pageable = createPageable(page, size, sortBy);
        return departmentRepository.findAllByType(parseType(type), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Department> findByParent(Long parentId, Integer page, Integer size, String sortBy) {
        Pageable pageable = createPageable(page, size, sortBy);
        return departmentRepository.findAllByParent_Id(parentId, pageable);
    }

    @Transactional(readOnly = true)
    public Set<Long> findParentIdsWithChildren(Collection<Long> parentIds) {
        if (parentIds == null || parentIds.isEmpty()) {
            return Set.of();
        }
        return new HashSet<>(departmentRepository.findParentIdsWithChildren(parentIds));
    }

    private Pageable createPageable(Integer page, Integer size, String sortBy) {
        return PageRequest.of(page, size, buildSort(sortBy));
    }

    private Sort buildSort(String sortBy) {
        String value = sortBy == null ? "ASC" : sortBy.trim().toUpperCase(Locale.ROOT);
        try {
            return Sort.by(Sort.Direction.valueOf(value), "name", "id");
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Направление сортировки " + sortBy + " не поддерживается. Используйте ASC или DESC");
        }
    }

    private DepartmentType parseType(String type) {
        try {
            return DepartmentType.valueOf(type);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Указанный тип департамента " + type + " не поддерживается");
        }
    }
}
