INSERT INTO department (name, type, parent_id)
VALUES ('Светлана-Полупроводники', 'FACTORY', null);
INSERT INTO department (name, type, parent_id)
VALUES ('Цех 79',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Светлана-Полупроводники'));

INSERT INTO department (name, type, parent_id)
VALUES ('Цех 69',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Светлана-Полупроводники'));

INSERT INTO department (name, type, parent_id)
VALUES ('СКТБ',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Светлана-Полупроводники'));

INSERT INTO department (name, type, parent_id)
VALUES ('Метрологическая служба',
        'WORKSHOP',
        (SELECT id FROM Department WHERE name = 'Светлана-Полупроводники'));

INSERT INTO department (name, type, parent_id)
VALUES ('Лаборатория поверки',
        'LABORATORY',
        (SELECT id FROM Department WHERE name = 'Метрологическая служба'));