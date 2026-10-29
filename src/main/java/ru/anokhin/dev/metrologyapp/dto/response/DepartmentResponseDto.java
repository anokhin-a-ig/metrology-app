package ru.anokhin.dev.metrologyapp.dto.response;

import ru.anokhin.dev.metrologyapp.dto.entity.DepartmentDto;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;

import java.time.LocalDateTime;
import java.util.List;

public record DepartmentResponseDto (
        Long id,
        String name,
        DepartmentType type,
        DepartmentDto parent,
        List<DepartmentDto> children,
        boolean hasChildren,
        boolean isArchive,
        LocalDateTime createAt,
        LocalDateTime archiveAt,
        LocalDateTime changeAt
){
}
