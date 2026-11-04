package ru.anokhin.dev.metrologyapp.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;
import ru.anokhin.dev.metrologyapp.exception.ArchiveDepartmentException;
import ru.anokhin.dev.metrologyapp.exception.DepartmentCycleException;
import ru.anokhin.dev.metrologyapp.exception.DepartmentNotFoundException;
import ru.anokhin.dev.metrologyapp.service.dto.DepartmentDtoService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentDtoService departmentDtoService;

    @Test
    @DisplayName("Пустое название отклоняется с 400")
    void changeById_blankNameReturns400() throws Exception {
        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "   ", "type": "WORKSHOP"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Название департамента обязательно"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Слишком длинное название отклоняется с 400")
    void changeById_tooLongNameReturns400() throws Exception {
        String longName = "а".repeat(256);

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + longName + "\",\"type\":\"WORKSHOP\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Название не должно превышать 255 символов"));
    }

    @Test
    @DisplayName("Отсутствующий тип отклоняется с 400")
    void changeById_missingTypeReturns400() throws Exception {
        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Некорректный тип департамента"));
    }

    @Test
    @DisplayName("Недопустимый тип отклоняется с 400")
    void changeById_invalidTypeReturns400() throws Exception {
        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "UNKNOWN"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Некорректный тип департамента"));
    }

    @Test
    @DisplayName("Битый JSON отклоняется с 400")
    void changeById_malformedJsonReturns400() throws Exception {
        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Некорректное тело запроса"));
    }

    @Test
    @DisplayName("Дубликат названия возвращает 400")
    void changeById_duplicateNameReturns400() throws Exception {
        when(departmentDtoService.changeById(eq(1L), any()))
                .thenThrow(new DataIntegrityViolationException(
                        "duplicate",
                        new SQLException(
                                "ERROR: duplicate key value violates unique constraint \"ux_department_name_lower_icu\"")));

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "WORKSHOP"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Департамент с таким названием уже существует"));
    }

    @Test
    @DisplayName("Отсутствующий департамент возвращает 404")
    void changeById_missingDepartmentReturns404() throws Exception {
        when(departmentDtoService.changeById(eq(1L), any()))
                .thenThrow(new DepartmentNotFoundException("Департамент с id: 1 не найден"));

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "WORKSHOP"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Департамент с id: 1 не найден"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Отсутствующий родитель возвращает 404")
    void changeById_missingParentReturns404() throws Exception {
        when(departmentDtoService.changeById(eq(1L), any()))
                .thenThrow(new DepartmentNotFoundException("Родительский департамент с id: 99 не найден"));

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "WORKSHOP", "parentId": 99}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Родительский департамент с id: 99 не найден"));
    }

    @Test
    @DisplayName("Самородитель возвращает 400")
    void changeById_selfParentReturns400() throws Exception {
        when(departmentDtoService.changeById(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Департамент не может быть родителем самого себя"));

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "WORKSHOP", "parentId": 1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Департамент не может быть родителем самого себя"));
    }

    @Test
    @DisplayName("Родитель-потомок возвращает 409")
    void changeById_descendantParentReturns409() throws Exception {
        when(departmentDtoService.changeById(eq(1L), any()))
                .thenThrow(new DepartmentCycleException(
                        "Нельзя переместить департамент внутрь его дочернего подразделения"));

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "WORKSHOP", "parentId": 2}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Нельзя переместить департамент внутрь его дочернего подразделения"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Архивный родитель возвращает 409")
    void changeById_archivedParentReturns409() throws Exception {
        when(departmentDtoService.changeById(eq(1L), any()))
                .thenThrow(new ArchiveDepartmentException("Родительский департамент удалён"));

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "WORKSHOP", "parentId": 2}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Родительский департамент удалён"));
    }

    @Test
    @DisplayName("Очистка родителя возвращает 200 и parent = null")
    void changeById_nullParentReturns200() throws Exception {
        DepartmentResponseDto response = new DepartmentResponseDto(
                1L,
                "Цех",
                DepartmentType.WORKSHOP,
                null,
                List.of(),
                false,
                false,
                LocalDateTime.now(),
                null,
                LocalDateTime.now());
        when(departmentDtoService.changeById(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Цех", "type": "WORKSHOP", "parentId": null}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parent").value(org.hamcrest.Matchers.nullValue()));
    }
}
