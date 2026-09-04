package ru.anokhin.dev.metrologyapp.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;

public record DepartmentRequestDto (
        @NotBlank(message = "Название департамента обязательно")
        String name,

        @NotNull(message = "Тип департамента обязателен")
        DepartmentType type,

        @Nullable
        Long parentId  // ← ID родительского департамента
){
}
