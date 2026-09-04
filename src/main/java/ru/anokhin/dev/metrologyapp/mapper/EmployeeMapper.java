package ru.anokhin.dev.metrologyapp.mapper;

import org.springframework.stereotype.Component;
import ru.anokhin.dev.metrologyapp.dto.entity.DepartmentDto;
import ru.anokhin.dev.metrologyapp.dto.request.EmployeeRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.EmployeeResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.Employee;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDto dto, Department department) {
        Employee emp = new Employee();
        emp.setFullName(dto.fullName());
        emp.setEmail(dto.email());
        emp.setEmployeeId(dto.employeeId());
        emp.setDismissalDate(dto.dismissalDate());
        emp.setDepartment(department);
        emp.setPosition(dto.position());
        emp.setSpecialty(dto.specialty());
        emp.setQualificationLevel(dto.qualificationLevel());
        emp.setCertifications(dto.certifications());
        emp.setSafetyTrainingDate(dto.safetyTrainingDate());
        emp.setAccessLevel(dto.accessLevel());
        emp.setActive(dto.isActive());
        emp.setStatus(dto.status());
        return emp;
    }

    public EmployeeResponseDto toDto(Employee e) {
        return new EmployeeResponseDto(
                e.getId(),
                e.getFullName(),
                e.getEmail(),
                e.getEmployeeId(),
                e.getDateOfEmployment(),
                e.getDismissalDate(),
                new DepartmentDto(e.getDepartment()),
                e.getPosition(),
                e.getSpecialty(),
                e.getQualificationLevel(),
                e.getCertifications(),
                e.getSafetyTrainingDate(),
                e.getAccessLevel(),
                e.isActive(),
                e.getStatus(),
                e.getMeasurements(),
                e.getPerformedVerifications()
        );
    }
}
