package ru.anokhin.dev.metrologyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.anokhin.dev.metrologyapp.entities.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("""
    SELECT d FROM Department d
    where d.name = :name
""")
    Optional<Department> findByName(@Param("name") String name);


    boolean existsByName(String name);
}
