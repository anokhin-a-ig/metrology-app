package ru.anokhin.dev.metrologyapp.mapper;

import org.springframework.stereotype.Component;
import ru.anokhin.dev.metrologyapp.dto.entity.DepartmentDto;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;

import java.util.List;

@Component
public class DepartmentMapper {

     public Department toEntity (DepartmentRequestDto dto, Department parent) {
         Department dep = new Department();
         dep.setName(dto.name() == null ? null : dto.name().trim());
         dep.setType(dto.type());
         dep.setParent(parent);
         return dep;
     }

    public DepartmentResponseDto toDto (Department dep) {
        List<DepartmentDto> children = dep.getChildren() != null
                ? dep.getChildren().stream().map(DepartmentDto::new).toList()
                : List.of();

        return new DepartmentResponseDto(
                dep.getId(),
                dep.getName(),
                dep.getType(),
                dep.getParent() != null ? new DepartmentDto(dep.getParent()) : null,
                children,
                !children.isEmpty(),
                dep.isArchive(),
                dep.getCreateAt(),
                dep.getArchiveAt(),
                dep.getChangeAt()
        );
    }

    public DepartmentResponseDto toDtoWithoutChildren(Department dep) {
        boolean hasChildren = dep.getChildren() != null && !dep.getChildren().isEmpty();
        return toDtoWithoutChildren(dep, hasChildren);
    }

    public DepartmentResponseDto toDtoWithoutChildren(Department dep, boolean hasChildren) {
         return new DepartmentResponseDto(
                 dep.getId(),
                 dep.getName(),
                 dep.getType(),
                 dep.getParent() != null ? new DepartmentDto(dep.getParent()) : null,
                 List.of(),
                 hasChildren,
                 dep.isArchive(),
                 dep.getCreateAt(),
                 dep.getArchiveAt(),
                 dep.getChangeAt()
         );
    }
}
