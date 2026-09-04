package ru.anokhin.dev.metrologyapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.anokhin.dev.metrologyapp.entities.enums.EmployeeStatus;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"certifications", "measurements", "performedVerifications"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор

    @Column(name = "full_name")
    private String fullName;    // ФИО

    @Column(unique = true)
    private String email;    // электронная почта

    @Column(name = "employee_id", unique = true)
    private String employeeId;    // табельный номер

    @Column(name = "date_of_employment", updatable = false)
    private LocalDate dateOfEmployment = LocalDate.now();    // дата приёма на работу

    @Column(name = "dismissal_date")
    private LocalDate dismissalDate;    // дата увольнения

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;    // цех/отдел


    private String position;   // должность

    private String specialty;    // специальность

    @Column(name = "qualification_level")
    private String qualificationLevel;    // разряд/категория

    @OneToMany(mappedBy = "employee")
    private List<Certificate> certifications;    // сертификаты

    @Column(name = "safety_training_date")
    private LocalDate safetyTrainingDate;    // дата обучения по охране труда

    @Column(name = "access_level")
    private String accessLevel;    // уровень допуска

    @Column(name = "is_active")
    private boolean isActive;    // работает / уволен

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;    // статус (на смене, в отпуске, на больничном)

    @OneToMany(mappedBy = "employeeOwner", fetch = FetchType.LAZY)
    private List<Measurement> measurements; // приборы, за которые отвечает работник

    @OneToMany(mappedBy = "metrologist", fetch = FetchType.LAZY)
    private List<Measurement> performedVerifications; // поверки, которые провёл работник
}
