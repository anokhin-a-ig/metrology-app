package ru.anokhin.dev.metrologyapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.anokhin.dev.metrologyapp.entities.Department;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("""
    SELECT d FROM Department d
    where d.name = :name
""")
    Optional<Department> findByName(@Param("name") String name);


    boolean existsByName(String name);

    Optional<List<Department>> findDepartmentByType(DepartmentType type);

    Page<Department> findAllByType(DepartmentType type, Pageable pageable);

    Page<Department> findAllByParent_Id(Long parentId, Pageable pageable);

    @Query("""
    SELECT DISTINCT d.parent.id FROM Department d
    WHERE d.parent.id IN :parentIds
""")
    List<Long> findParentIdsWithChildren(@Param("parentIds") Collection<Long> parentIds);
}
