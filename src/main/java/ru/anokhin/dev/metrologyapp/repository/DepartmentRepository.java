package ru.anokhin.dev.metrologyapp.repository;

import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anokhin.dev.metrologyapp.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
