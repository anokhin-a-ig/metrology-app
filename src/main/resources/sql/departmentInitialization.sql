INSERT INTO department (name, type, parent_id, create_at, change_at, archive)
VALUES ('Завод ЭлектронПрибор', 'FACTORY', null, now(), now(), false);

INSERT INTO department (name, type, parent_id, create_at, change_at, archive)
VALUES ('Цех сборки',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Завод ЭлектронПрибор'), now(), now(), false);

INSERT INTO department (name, type, parent_id, create_at, change_at, archive)
VALUES ('Лаборатория электрических измерений',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Цех сборки'), now(), now(), false);
