INSERT INTO department (name, type, parent_id, create_at, change_at)
VALUES ('Завод ЭлектронПрибор', 'FACTORY', null, now(), now());

INSERT INTO department (name, type, parent_id, create_at, change_at)
VALUES ('Цех сборки',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Завод ЭлектронПрибор'), now(), now());

INSERT INTO department (name, type, parent_id, create_at, change_at)
VALUES ('Цех механической обработки',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Завод ЭлектронПрибор'), now(), now());

INSERT INTO department (name, type, parent_id, create_at, change_at)
VALUES ('Отдел главного метролога',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Завод ЭлектронПрибор'), now(), now());

INSERT INTO department (name, type, parent_id, create_at, change_at)
VALUES ('Лаборатория электрических измерений',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Отдел главного метролога'), now(), now());

INSERT INTO department (name, type, parent_id, create_at, change_at)
VALUES ('Лаборатория радиотехнических измерений',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Отдел главного метролога'), now(), now());

INSERT INTO department (name, type, parent_id, create_at, change_at)
VALUES ('Лаборатория тепловых измерений',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Отдел главного метролога'), now(), now());
