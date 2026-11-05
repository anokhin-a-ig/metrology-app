package ru.anokhin.dev.metrologyapp.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ru.anokhin.dev.metrologyapp.entities.Certificate;
import ru.anokhin.dev.metrologyapp.entities.enums.EmployeeStatus;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EmployeeRequestDto(
        String firstName,    // ФИО
        String lastName,    // ФИО
        String middleName,    // ФИО
        String email,    // электронная почта
        String factoryNumber,    // табельный номер
        LocalDate dismissalDate,    // дата увольнения
        Long departmentId,    // цех/отдел
        String position,   // должность
        String specialty,    // специальность
        String qualificationLevel,    // разряд/категория
        List<Certificate> certifications,    // сертификаты
        LocalDate safetyTrainingDate,    // дата обучения по охране труда
        String accessLevel,    // уровень допуска
        boolean isActive,    // работает / уволен
        EmployeeStatus status    // статус (на смене, в отпуске, на больничном)
) {
}
