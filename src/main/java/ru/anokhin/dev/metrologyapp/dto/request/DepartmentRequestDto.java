package ru.anokhin.dev.metrologyapp.dto.request;

import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;

public record DepartmentRequestDto (
        String name,
        DepartmentType type,
        Long parentId  // ← ID родительского департамента
){
}
