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

    EmployeeService(EmployeeRepository repo) {
        this.employeeRepository = repo;
    }

    public Employee save(Employee employee) {
       return employeeRepository.save(employee);
    }

    public boolean existByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public boolean existByFactoryNumber(String s) {
        return employeeRepository.existsByFactoryNumber(s);
    }
}
