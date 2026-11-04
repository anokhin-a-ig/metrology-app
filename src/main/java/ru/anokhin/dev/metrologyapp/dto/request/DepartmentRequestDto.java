package ru.anokhin.dev.metrologyapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;

public record DepartmentRequestDto (
        @NotBlank(message = "Название департамента обязательно")
        @Size(max = 255, message = "Название не должно превышать 255 символов")
        String name,
        @NotNull(message = "Некорректный тип департамента")
        DepartmentType type,
        Long parentId  // ← ID родительского департамента
){
}
