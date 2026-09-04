package ru.anokhin.dev.metrologyapp.dto.entity;

import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;

public record DepartmentDto(
        Long id,
        String name,
        DepartmentType type
) {
    public DepartmentDto(Department department) {
        this(
                department != null ? department.getId() : null,
                department != null ? department.getName() : null,
                department != null ? department.getType() : null
        );
    }
}
