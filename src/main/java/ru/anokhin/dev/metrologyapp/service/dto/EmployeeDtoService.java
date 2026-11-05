package ru.anokhin.dev.metrologyapp.service.dto;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anokhin.dev.metrologyapp.dto.request.EmployeeRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.EmployeeResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.Employee;
import ru.anokhin.dev.metrologyapp.exception.employee.EmployeeEmailAlreadyExistException;
import ru.anokhin.dev.metrologyapp.exception.employee.EmployeeFactoryNumberAlreadyExistException;
import ru.anokhin.dev.metrologyapp.mapper.EmployeeMapper;
import ru.anokhin.dev.metrologyapp.service.DepartmentService;
import ru.anokhin.dev.metrologyapp.service.EmployeeService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class EmployeeDtoService {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final DepartmentService departmentService;

    public EmployeeDtoService(EmployeeService employeeService, EmployeeMapper employeeMapper, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.departmentService = departmentService;
    }


    public EmployeeResponseDto save(EmployeeRequestDto dto) {
        Department department = departmentService.findById(dto.departmentId());
        if (employeeService.existByEmail(dto.email())) {
            throw new EmployeeEmailAlreadyExistException("Email занят. Необходимо выбрать уникальный");
        }
        if (employeeService.existByFactoryNumber(dto.factoryNumber())) {
            throw new EmployeeFactoryNumberAlreadyExistException("Табельный номер сотрудника занят. Необходимо выбрать уникальный");
        }
        Employee employee = employeeMapper.toEntity(dto, department);
        employee.setActive(true);
        employee.setDateOfEmployment(LocalDate.now());
        employeeService.save(employee);
        return employeeMapper.toDto(employee);
    }
}
