package ru.anokhin.dev.metrologyapp.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;
import ru.anokhin.dev.metrologyapp.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentService service;

    @Test
    @DisplayName("Поиск департамента по его ID. Найден")
    void findById_positiveResult() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Lab1");
        department.setType(DepartmentType.FACTORY);
        department.setParent(null);
        department.setChildren(null);

        when(repository.findById(1L)).thenReturn(Optional.of(department));

        Department dep = service.findById(1L);

        assertNotNull(dep);
        assertEquals("Lab1", dep.getName());
        assertNotNull(dep.getType());
        assertNull(dep.getChildren());
        assertNull(dep.getParent());
    }


    @Test
    @DisplayName("Поиск департамента по его ID. Не найден")
    void findById_negativeResult() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(2L));
        verify(repository).findById(2L);
    }

    @Test
    @DisplayName("Проверка существования департамента обрезает пробелы")
    void existDepartment_trimsName() {
        when(repository.existsByNameCaseInsensitive("Цех")).thenReturn(true);

        assertTrue(service.existDepartment("  Цех  "));
        verify(repository).existsByNameCaseInsensitive("Цех");
    }

    @Test
    @DisplayName("Проверка существования департамента с null не обращается к репозиторию")
    void existDepartment_nullName() {
        assertFalse(service.existDepartment((String) null));
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Поиск по названию обрезает пробелы")
    void searchByName_trimsSearch() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Цех сборки");
        department.setType(DepartmentType.WORKSHOP);

        when(repository.searchByNameCaseInsensitive("цех")).thenReturn(List.of(department));

        List<Department> result = service.searchByName("  цех  ");

        assertEquals(1, result.size());
        assertEquals("Цех сборки", result.get(0).getName());
        verify(repository).searchByNameCaseInsensitive("цех");
    }
}
