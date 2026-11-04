# Spec Delta

## MODIFIED Requirements

### Requirement: Update department via PUT

Бэкенд SHALL обновлять департамент запросом `PUT /api/v1/departments/{id}` с
телом `{ name, type, parentId? }` и возвращать `200` с обновлённым
`DepartmentResponseDto`. Для типа не-`FACTORY` поле `parentId` SHALL быть
обязательным.

#### Scenario: Успешное обновление

- **WHEN** запрос содержит существующий `id`, непустое `name`, допустимый `type` и `parentId`
- **THEN** бэкенд сохраняет изменения и возвращает `200` с объектом, содержащим `id`, `name`, `type`, `parent`, `children`, `hasChildren`, `isArchive`, `createAt`, `archiveAt`, `changeAt`

#### Scenario: Обновление без родителя

- **WHEN** `type` равен `FACTORY`, а `parentId` отсутствует или равен `null`
- **THEN** бэкенд сохраняет департамент без родителя и возвращает `200`

#### Scenario: Обновление не-FACTORY без родителя

- **WHEN** `type` равен `WORKSHOP`, `AREA` или `LABORATORY`, а `parentId` отсутствует или равен `null`
- **THEN** бэкенд возвращает `400` с сообщением `Родительский департамент обязателен`

#### Scenario: Тип передаётся всегда

- **WHEN** интерфейс не меняет тип департамента
- **THEN** он всё равно передаёт текущий `type` в теле запроса

## ADDED Requirements

### Requirement: Require parent for non-FACTORY on create

Бэкенд SHALL требовать `parentId` при создании департамента типа `WORKSHOP`,
`AREA` или `LABORATORY`; создание `FACTORY` SHALL оставаться без родителя.

#### Scenario: Создание не-FACTORY без родителя

- **WHEN** тело запроса содержит `type` `WORKSHOP`, `AREA` или `LABORATORY` и не содержит `parentId`
- **THEN** бэкенд возвращает `400` с сообщением `Родительский департамент обязателен`

#### Scenario: Создание не-FACTORY с родителем

- **WHEN** тело запроса содержит не-`FACTORY` тип и существующий `parentId`
- **THEN** бэкенд создаёт департамент и возвращает `201`

#### Scenario: Создание FACTORY

- **WHEN** тело запроса содержит `type` `FACTORY` без `parentId`
- **THEN** бэкенд создаёт корневой департамент и возвращает `201`
