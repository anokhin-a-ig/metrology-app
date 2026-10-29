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


    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM department d
                WHERE lower(d.name COLLATE "ru-RU-x-icu") =
                      lower(CAST(:name AS text) COLLATE "ru-RU-x-icu")
            )
            """, nativeQuery = true)
    boolean existsByNameCaseInsensitive(@Param("name") String name);

    Optional<List<Department>> findDepartmentByType(DepartmentType type);

    Page<Department> findAllByType(DepartmentType type, Pageable pageable);

    Page<Department> findAllByParent_Id(Long parentId, Pageable pageable);

    @Query("""
    SELECT DISTINCT d.parent.id FROM Department d
    WHERE d.parent.id IN :parentIds
      AND d.isArchive = false
""")
    List<Long> findParentIdsWithChildren(@Param("parentIds") Collection<Long> parentIds);

    @Query(value = """
            SELECT d.*
            FROM department d
            WHERE strpos(
                lower(d.name COLLATE "ru-RU-x-icu"),
                lower(CAST(:search AS text) COLLATE "ru-RU-x-icu")
            ) > 0 AND d.archive = false
            ORDER BY d.name COLLATE "ru-RU-x-icu", d.id
            """, nativeQuery = true)
    List<Department> searchByNameCaseInsensitive(@Param("search") String search);


    List<Department> findAllByIsArchiveIsFalse();

    Page<Department> findAllByIsArchiveIsFalse(boolean isArchive, Pageable pageable);

    Page<Department> findAllByTypeAndIsArchiveIsFalse(DepartmentType departmentType, Pageable pageable);

    Page<Department> findAllByParent_IdAndIsArchive(Long parentId, boolean isArchive, Pageable pageable);
}
