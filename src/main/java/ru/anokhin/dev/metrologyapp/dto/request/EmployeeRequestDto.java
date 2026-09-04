package ru.anokhin.dev.metrologyapp.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.anokhin.dev.metrologyapp.entities.Certificate;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.enums.EmployeeStatus;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EmployeeRequestDto(

        @NotBlank(message = "ФИО обязательно")
        String fullName,    // ФИО

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        String email,    // электронная почта

        @NotBlank(message = "Табельный номер обязателен")
        String employeeId,    // табельный номер

        @Nullable LocalDate dismissalDate,    // дата увольнения

        @NotNull(message = "ID департамента обязателен")
        Long departmentId,    // цех/отдел

        @NotBlank(message = "Должность обязательна")
        String position,   // должность

        @NotBlank(message = "Специальность обязательна")
        String specialty,    // специальность

        @Nullable String qualificationLevel,    // разряд/категория
        @Nullable List<Certificate> certifications,    // сертификаты
        @Nullable LocalDate safetyTrainingDate,    // дата обучения по охране труда
        @Nullable String accessLevel,    // уровень допуска
        boolean isActive,    // работает / уволен
        @Nullable EmployeeStatus status    // статус (на смене, в отпуске, на больничном)
) {
}
