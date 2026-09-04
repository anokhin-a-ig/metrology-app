package ru.anokhin.dev.metrologyapp.dto.response;

import ru.anokhin.dev.metrologyapp.dto.entity.DepartmentDto;
import ru.anokhin.dev.metrologyapp.entities.Certificate;
import ru.anokhin.dev.metrologyapp.entities.Measurement;
import ru.anokhin.dev.metrologyapp.entities.enums.EmployeeStatus;

import java.time.LocalDate;
import java.util.List;

public record EmployeeResponseDto(

        Long id, //уникальный идентификатор
        String fullName,    // ФИО
        String email,    // электронная почта
        String employeeId,    // табельный номер
        LocalDate dateOfEmployment,// дата приёма на работу
        LocalDate dismissalDate,    // дата увольнения
        DepartmentDto department,    // цех/отдел
        String position,   // должность
        String specialty,    // специальность
        String qualificationLevel,    // разряд/категория
        List<Certificate> certifications,    // сертификаты   // TODO: заменить на DTO
        LocalDate safetyTrainingDate,    // дата обучения по охране труда
        String accessLevel,    // уровень допуска
        boolean isActive,    // работает / уволен
        EmployeeStatus status,    // статус (на смене, в отпуске, на больничном)
        List<Measurement> measurements, // приборы, за которые отвечает работник   // TODO: заменить на DTO
        List<Measurement> performedVerifications // поверки, которые провёл работник   // TODO: заменить на DTO
) {
}
