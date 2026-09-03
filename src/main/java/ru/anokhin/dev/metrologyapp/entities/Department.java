package ru.anokhin.dev.metrologyapp.entities;



import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "name", nullable = false)
    private String name; // наименование (например, "Цех №69", "Участок 1", "Лаборатория метрологии")

    @Column(name = "type", nullable = false)
    private String type; // тип: factory (завод), workshop (цех), area (участок), lab (лаборатория)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent; // родительское подразделение

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> children = new ArrayList<>(); // дочерние подразделения
}
