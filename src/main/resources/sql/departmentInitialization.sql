INSERT INTO department (name, type, parent_id)
VALUES ('Завод ЭлектронПрибор', 'FACTORY', null);

INSERT INTO department (name, type, parent_id)
VALUES ('Цех сборки',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Завод ЭлектронПрибор'));

INSERT INTO department (name, type, parent_id)
VALUES ('Цех механической обработки',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Завод ЭлектронПрибор'));

INSERT INTO department (name, type, parent_id)
VALUES ('Отдел главного метролога',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Завод ЭлектронПрибор'));

INSERT INTO department (name, type, parent_id)
VALUES ('Лаборатория электрических измерений',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Отдел главного метролога'));

INSERT INTO department (name, type, parent_id)
VALUES ('Лаборатория радиотехнических измерений',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Отдел главного метролога'));

INSERT INTO department (name, type, parent_id)
VALUES ('Лаборатория тепловых измерений',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Отдел главного метролога'));
