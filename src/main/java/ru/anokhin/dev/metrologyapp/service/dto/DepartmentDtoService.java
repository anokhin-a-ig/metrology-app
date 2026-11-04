package ru.anokhin.dev.metrologyapp.service.dto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anokhin.dev.metrologyapp.dto.entity.DepartmentDto;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.exception.ArchiveDepartmentException;
import ru.anokhin.dev.metrologyapp.exception.DepartmentCycleException;
import ru.anokhin.dev.metrologyapp.exception.DepartmentNotFoundException;
import ru.anokhin.dev.metrologyapp.exception.DuplicateDepartmentException;
import ru.anokhin.dev.metrologyapp.mapper.DepartmentMapper;
import ru.anokhin.dev.metrologyapp.service.DepartmentService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (departmentService.existDepartment(dto.name())) {
            throw new DuplicateDepartmentException("Департамент с таким названием уже существует");
        }
        Department parent = resolveParent(dto.parentId());

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

    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> findAllWithPagination(Integer offset, Integer limit) {
        Page<Department> page = departmentService.findAllWithPagination(offset, limit);
        return page.stream().map(departmentMapper::toDtoWithoutChildren).toList();
    }

    public DepartmentResponseDto changeById(Long id, DepartmentRequestDto dto) {
        Department department = departmentService.findById(id);
        if (department.isArchive()) {
            throw new ArchiveDepartmentException("Департамент удалён и не может быть изменён");
        }

        if (id.equals(dto.parentId())) {
            throw new IllegalArgumentException("Департамент не может быть родителем самого себя");
        }

        Department parent = resolveParent(dto.parentId());
        if (parent != null) {
            if (parent.isArchive()) {
                throw new ArchiveDepartmentException("Родительский департамент удалён");
            }
            if (isDescendantOf(parent, id)) {
                throw new DepartmentCycleException(
                        "Нельзя переместить департамент внутрь его дочернего подразделения");
            }
        }

        department.setId(id);
        department.setName(dto.name() == null ? null : dto.name().trim());
        department.setType(dto.type());
        department.setParent(parent);
        departmentService.saveAndFlush(department);
        return departmentMapper.toDto(department);
    }

    private Department resolveParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        try {
            return departmentService.findById(parentId);
        } catch (DepartmentNotFoundException ex) {
            throw new DepartmentNotFoundException("Родительский департамент с id: " + parentId + " не найден");
        }
    }

    private boolean isDescendantOf(Department candidateParent, Long departmentId) {
        Set<Long> visited = new HashSet<>();
        Department current = candidateParent;
        while (current != null && visited.add(current.getId())) {
            if (departmentId.equals(current.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    public void delete(Long id) {
        Department department = departmentService.findById(id);
        boolean canArchive = true; //сразу говорим что можно удалять
        if (department != null) { // департамент найден
            if (!department.isArchive()) { // и он не архивный
                if (!department.getChildren().isEmpty()) { // у него есть дети
                    List<Department> children = department.getChildren(); // получаем всех детей
                    for (Department d : children) { // проверяем каждого ребенка
                        if (!d.isArchive()) { //если хоть 1 не архивный
                            canArchive =  false; // закрываем возможность архивировать
                            break;
                        }
                    }
                }
            } else {
                throw new ArchiveDepartmentException("Департамент уже удален. Повторное удаление невозможно");
            }
//            if (canArchive) {
//                department.setArchive(true);
//                department.setArchiveAt(LocalDateTime.now());
//                departmentService.save(department);
//            } else {
//                throw new ArchiveDepartmentException("Департамент уже удален. Повторное удаление невозможно");
//            }
        } else {
            throw new IllegalArgumentException("Департамента с id:" + id + "не существует");
        }
        if (canArchive) {
            department.setArchiveAt(LocalDateTime.now());
            department.setArchive(true);
            departmentService.save(department);
        }
    }

    @Transactional(readOnly = true)
    public DepartmentResponseDto findByName(String name) {
        return departmentMapper.toDto(departmentService.findByName(name));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> findByType(String type) {
        return departmentService.findByType(type).stream().map(departmentMapper::toDtoWithoutChildren).toList();
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponseDto> findAllWithPageable(Integer page, Integer size, String type, String sortBy) {
        return toDtoPage(departmentService.findAllWithPageable(page, size, type, sortBy));
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponseDto> findByParent(Long parentId, Integer page, Integer size, String sortBy) {
        return toDtoPage(departmentService.findByParent(parentId, page, size, sortBy));
    }

    private Page<DepartmentResponseDto> toDtoPage(Page<Department> page) {
        Set<Long> parentIdsWithChildren = departmentService.findParentIdsWithChildren(
                page.stream().map(Department::getId).toList());
        return page.map(department -> departmentMapper.toDtoWithoutChildren(
                department, parentIdsWithChildren.contains(department.getId())));
    }

    @Transactional(readOnly = true)
    public List<DepartmentDto> searchByName(String search) {
        return departmentService.searchByName(search)
                .stream()
                .map(DepartmentDto::new)
                .toList();
    }
}
