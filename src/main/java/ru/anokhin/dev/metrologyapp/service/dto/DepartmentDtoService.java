package ru.anokhin.dev.metrologyapp.service.dto;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.mapper.DepartmentMapper;
import ru.anokhin.dev.metrologyapp.service.DepartmentService;

import java.util.List;

@Service
@Transactional
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

    @Transactional(readOnly = true)
    public DepartmentResponseDto findById(Long id) {
        return departmentMapper.toDto(departmentService.findById(id));
    }

    @Transactional(readOnly = true)
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

    public void delete(Long id) {
        Department department = departmentService.findById(id);
        if (department != null) {
            if(department.getChildren().isEmpty()) {
                departmentService.delete(id);
            } else {
                throw new IllegalArgumentException("Удалите или переназначьте сначала зависимые департаменты");
            }
        } else {
            throw new IllegalArgumentException("Департамента с id:" + id + "не существует");
        }
    }
}
