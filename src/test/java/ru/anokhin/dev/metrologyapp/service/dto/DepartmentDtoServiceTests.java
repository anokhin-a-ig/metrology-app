package ru.anokhin.dev.metrologyapp.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;
import ru.anokhin.dev.metrologyapp.exception.ArchiveDepartmentException;
import ru.anokhin.dev.metrologyapp.exception.DepartmentCycleException;
import ru.anokhin.dev.metrologyapp.exception.DepartmentNotFoundException;
import ru.anokhin.dev.metrologyapp.mapper.DepartmentMapper;
import ru.anokhin.dev.metrologyapp.service.DepartmentService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentDtoServiceTests {

    @Mock
    private DepartmentService departmentService;

    @Spy
    private DepartmentMapper departmentMapper = new DepartmentMapper();

    @InjectMocks
    private DepartmentDtoService service;

    @Test
    @DisplayName("Дубликат имени определяется ограничением БД, предварительная проверка не используется")
    void changeById_duplicateNameUsesDatabaseConstraint() {
        Department department = department(1L, "Цех", false);
        when(departmentService.findById(1L)).thenReturn(department);
        DataIntegrityViolationException failure = new DataIntegrityViolationException(
                "duplicate",
                new SQLException("ERROR: duplicate key value violates unique constraint \"ux_department_name_lower_icu\""));
        when(departmentService.saveAndFlush(department)).thenThrow(failure);

        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> service.changeById(1L, dto("Другой цех", null)));

        assertSame(failure, thrown);
        verify(departmentService, never()).existDepartment(anyString());
    }

    @Test
    @DisplayName("Сохранение без изменения имени проходит")
    void changeById_sameNameSucceeds() {
        Department department = department(1L, "Цех", false);
        when(departmentService.findById(1L)).thenReturn(department);
        when(departmentService.saveAndFlush(department)).thenReturn(department);

        DepartmentResponseDto result = service.changeById(1L, dto("Цех", null));

        assertEquals("Цех", result.name());
        verify(departmentService).saveAndFlush(department);
    }

    @Test
    @DisplayName("Смена регистра собственного названия проходит")
    void changeById_caseChangeSucceeds() {
        Department department = department(1L, "Цех", false);
        when(departmentService.findById(1L)).thenReturn(department);
        when(departmentService.saveAndFlush(department)).thenReturn(department);

        DepartmentResponseDto result = service.changeById(1L, dto("цЕХ", null));

        assertEquals("цЕХ", result.name());
        verify(departmentService).saveAndFlush(department);
    }

    @Test
    @DisplayName("Родитель, указывающий на самого себя, отклоняется с 400")
    void changeById_selfParentRejected() {
        Department department = department(1L, "Цех", false);
        when(departmentService.findById(1L)).thenReturn(department);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.changeById(1L, dto("Цех", 1L)));

        assertEquals("Департамент не может быть родителем самого себя", exception.getMessage());
        verify(departmentService, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Родитель-потомок отклоняется с 409")
    void changeById_descendantParentRejected() {
        Department root = department(1L, "Завод", false);
        Department child = department(2L, "Цех", false);
        child.setParent(root);
        Department grandchild = department(3L, "Участок", false);
        grandchild.setParent(child);

        when(departmentService.findById(1L)).thenReturn(root);
        when(departmentService.findById(3L)).thenReturn(grandchild);

        DepartmentCycleException exception = assertThrows(
                DepartmentCycleException.class,
                () -> service.changeById(1L, dto("Завод", 3L)));

        assertEquals("Нельзя переместить департамент внутрь его дочернего подразделения", exception.getMessage());
        verify(departmentService, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Архивный департамент нельзя изменить")
    void changeById_archivedDepartmentRejected() {
        Department department = department(1L, "Цех", true);
        when(departmentService.findById(1L)).thenReturn(department);

        ArchiveDepartmentException exception = assertThrows(
                ArchiveDepartmentException.class,
                () -> service.changeById(1L, dto("Цех", null)));

        assertEquals("Департамент удалён и не может быть изменён", exception.getMessage());
        verify(departmentService, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Архивный родитель отклоняется с 409")
    void changeById_archivedParentRejected() {
        Department department = department(1L, "Цех", false);
        Department parent = department(2L, "Архивный цех", true);
        when(departmentService.findById(1L)).thenReturn(department);
        when(departmentService.findById(2L)).thenReturn(parent);

        ArchiveDepartmentException exception = assertThrows(
                ArchiveDepartmentException.class,
                () -> service.changeById(1L, dto("Цех", 2L)));

        assertEquals("Родительский департамент удалён", exception.getMessage());
        verify(departmentService, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Отсутствующий родитель даёт 404 с точным сообщением")
    void changeById_missingParentReportsParentMessage() {
        Department department = department(1L, "Цех", false);
        when(departmentService.findById(1L)).thenReturn(department);
        when(departmentService.findById(99L))
                .thenThrow(new DepartmentNotFoundException("Департамент с id: 99 не найден"));

        DepartmentNotFoundException exception = assertThrows(
                DepartmentNotFoundException.class,
                () -> service.changeById(1L, dto("Цех", 99L)));

        assertEquals("Родительский департамент с id: 99 не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Отсутствующий департамент даёт 404")
    void changeById_missingDepartmentPropagatesNotFound() {
        when(departmentService.findById(1L))
                .thenThrow(new DepartmentNotFoundException("Департамент с id: 1 не найден"));

        DepartmentNotFoundException exception = assertThrows(
                DepartmentNotFoundException.class,
                () -> service.changeById(1L, dto("Цех", null)));

        assertEquals("Департамент с id: 1 не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Очистка родителя выставляет parent = null")
    void changeById_nullParentClearsParent() {
        Department department = department(1L, "Цех", false);
        department.setParent(department(2L, "Завод", false));
        when(departmentService.findById(1L)).thenReturn(department);
        when(departmentService.saveAndFlush(department)).thenReturn(department);

        DepartmentResponseDto result = service.changeById(1L, dto("Цех", null));

        assertNull(result.parent());
        assertNull(department.getParent());
        verify(departmentService).saveAndFlush(department);
    }

    private Department department(Long id, String name, boolean archive) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        department.setType(DepartmentType.WORKSHOP);
        department.setArchive(archive);
        return department;
    }

    private DepartmentRequestDto dto(String name, Long parentId) {
        return new DepartmentRequestDto(name, DepartmentType.WORKSHOP, parentId);
    }
}
