package ru.anokhin.dev.metrologyapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.anokhin.dev.metrologyapp.entities.enums.VerificationMethod;
import ru.anokhin.dev.metrologyapp.entities.enums.VerificationStatus;

import java.time.LocalDate;

@Entity
@Table(name = "measurement")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор

    @Column(name = "inventory_number")
    private String inventoryNumber; // инвентарный номер - внутренний номер на заводе

    @Column(name = "factory_number")
    private String factoryNumber; // заводской номер - серийный номер от производителя

    @Column(name = "name")
    private String name; // наименование - полное название прибора

    @Column(name = "type")
    private String type; // тип/модель - тип или модель СИ

    @Column(name = "manufacture_creator")
    private String manufactureCreator; // производитель - компания-изготовитель

    @Column(name = "range")
    private String range; // диапазон измерений - минимальное и максимальное значение

    @Column(name = "accuracy")
    private String accuracy; // класс точности/погрешность - метрологические характеристики

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement; // единица измерения - В, А, °C, Па и т.д.

    @Column(name = "resolution")
    private String resolution; // разрядность/разрешение - если применимо

    @Column(name = "certificate")
    private String certificate; // номер свидетельства о поверке - документ подтверждающий поверку

    @Column(name = "last_verification_date")
    private LocalDate lastVerificationDate; // дата последней поверки - когда проводилась

    @Column(name = "next_verification_date")
    private LocalDate nextVerificationDate; //дата следующей поверки - срок действия поверки

    @Column(name = "calibration_interval")
    private Long calibrationInterval; // межповерочный интервал - периодичность поверки (месяцы/годы)

    @Enumerated(EnumType.STRING)
    private VerificationMethod verificationMethod; // метод поверки - первичная/периодическая/внеочередная

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metrologist_id")
    private Employee metrologist; // метролог - кто выполнял поверку

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VerificationStatus status; // статус - годен/не годен/на поверке/в ремонте

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_owner_id")
    private Department departmentOwner; // подразделение - где используется (цех, лаборатория)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_owner_id")
    private Employee employeeOwner; // ответственное лицо - кто отвечает за прибор

    @Column(name = "date_of_commissioning")
    private LocalDate dateOfCommissioning; // дата ввода в эксплуатацию

    @Column(name = "note")
    private String note; // примечания - дополнительная информация
}
