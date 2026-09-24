package ru.anokhin.dev.metrologyapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anokhin.dev.metrologyapp.dto.request.EmployeeRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.EmployeeResponseDto;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.Employee;
import ru.anokhin.dev.metrologyapp.mapper.EmployeeMapper;
import ru.anokhin.dev.metrologyapp.repository.EmployeeRepository;

import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final EmployeeMapper employeeMapper;

    EmployeeService(EmployeeRepository repo, DepartmentService departmentService, EmployeeMapper employeeMapper) {
        this.employeeRepository = repo;
        this.departmentService = departmentService;
        this.employeeMapper = employeeMapper;
    }

    public EmployeeResponseDto save(EmployeeRequestDto dto) {
        employeeRepository.findEmployeeByEmail(dto.email())
                .ifPresent(employee -> {
                    throw new IllegalArgumentException("Сотрудник с таким email уже существует");
                });

        employeeRepository.findEmployeeByEmployeeId(dto.employeeId())
                .ifPresent(employee -> {
                    throw new IllegalArgumentException("Сотрудник с таким табельным номером уже существует");
                });

        Department department = departmentService.findById(dto.departmentId());

        Employee employee = employeeMapper.toEntity(dto, department);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }
}
