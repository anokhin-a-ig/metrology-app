package ru.anokhin.dev.metrologyapp.entities;



import jakarta.persistence.*;
import lombok.*;
import ru.anokhin.dev.metrologyapp.entities.enums.DepartmentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"parent", "children"})
@ToString(exclude = {"parent", "children"})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; // наименование (например, "Цех №69", "Участок 1", "Лаборатория метрологии")

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartmentType type; // тип: factory (завод), workshop (цех), area (участок), lab (лаборатория)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent; // родительское подразделение

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> children = new ArrayList<>(); // дочерние подразделения

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "archive")
    private boolean isArchive;

    @Column(name = "archive_at")
    private LocalDateTime archiveAt;

    @Column(name = "change_at")
    private LocalDateTime changeAt;

    @PrePersist
    public void onPersist() {
        this.setCreateAt(LocalDateTime.now());
        this.setChangeAt(LocalDateTime.now());
        this.setArchive(false);
    }

    @PreUpdate
    public void onUpdate() {
        this.setChangeAt(LocalDateTime.now());
    }
}
