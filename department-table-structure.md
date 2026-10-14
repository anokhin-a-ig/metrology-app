# Структура таблицы `department`

## Описание

Таблица хранит иерархию подразделений предприятия (завод → цех → лаборатория). Создаётся и заполняется через Liquibase.

## Источники

- **Changelog:** `src/main/resources/db/changelog/1-0-0-SNAPSHOT/01-init-db-table-changelog.xml` (changeSet `1788899902736-1`, `1788899902736-5`, `1788899902736-8`)
- **Данные:** `src/main/resources/sql/departmentInitialization.sql` (подключается в `02-init-data-in-table.xml`)

## Колонки

| Колонка     | Тип          | Nullable | Ограничения          | Описание                                      |
|-------------|--------------|----------|----------------------|-----------------------------------------------|
| `id`        | BIGINT       | NOT NULL | PK, AUTO_INCREMENT   | Идентификатор подразделения                    |
| `name`      | VARCHAR(255) | NOT NULL | UNIQUE (`uc_department_name`) | Наименование (например, «Цех №69») |
| `type`      | VARCHAR(255) | NOT NULL | —                    | Тип: `FACTORY`, `WORKSHOP`, `AREA`, `LABORATORY` |
| `parent_id` | BIGINT       | NULL     | FK → `department.id` | Родительское подразделение (ссылка на себя)    |

## Связи

| Ограничение                      | Тип | Описание                                    |
|----------------------------------|-----|---------------------------------------------|
| `pk_department`                  | PK  | Первичный ключ по `id`                      |
| `uc_department_name`             | UK  | Уникальность `name`                         |
| `FK_DEPARTMENT_ON_PARENT`        | FK  | `parent_id` → `department.id` (самоссылка)  |

## Иерархия подразделений (тестовые данные)

```
Завод ЭлектронПрибор (FACTORY)
├── Цех сборки (WORKSHOP)
├── Цех механической обработки (WORKSHOP)
└── Отдел главного метролога (WORKSHOP)
    ├── Лаборатория электрических измерений (LABORATORY)
    ├── Лаборатория радиотехнических измерений (LABORATORY)
    └── Лаборатория тепловых измерений (LABORATORY)
```

## JPA-сущность

`src/main/java/ru/anokhin/dev/metrologyapp/entities/Department.java`

```java
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartmentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> children = new ArrayList<>();
}
```
