
-- autopark, 7 tables creation in order than given below...

CREATE TABLE drivers (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL,
  firstname VARCHAR(64) NOT NULL,
  lastname VARCHAR(64) NOT NULL,
  phone1 VARCHAR(24) NOT NULL,
  phone2 VARCHAR(24) NULL,
  phone_relatives VARCHAR(24) NULL,
  email VARCHAR(255) NULL,
  PRIMARY KEY (id));

CREATE TABLE driving_licence_categories (
  id INT NOT NULL AUTO_INCREMENT,
  licence_category VARCHAR(8) NOT NULL,
  category_description VARCHAR(255) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE auto_manufacturer (
	id INT NOT NULL AUTO_INCREMENT,
    manufacturer_name VARCHAR(64) NOT NULL,
    description VARCHAR(255) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE auto (
  id INT NOT NULL AUTO_INCREMENT,
  manufacturer INT NOT NULL,
  model VARCHAR(64) NOT NULL,
  vin_number VARCHAR(32) NOT NULL,
  driving_licence_category INT NOT NULL,
  engine_model VARCHAR(64) NOT NULL,
  engine_power INT NOT NULL,
  engine_eco INT NOT NULL,
  gearbox VARCHAR(64) NOT NULL,
  chassis_type VARCHAR(64) NOT NULL,
  max_weight INT NOT NULL,
  equipped_weight INT NOT NULL,
  license_plate_number VARCHAR(32) NOT NULL,
  description VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  foreign key (manufacturer) references auto_manufacturer(id),
  foreign key (driving_licence_category) references driving_licence_categories(id)
);

CREATE TABLE driver_auto (
    driver_id INT NOT NULL,
    auto_id INT NOT NULL,
    PRIMARY KEY (driver_id, auto_id),
    CONSTRAINT fk_driver FOREIGN KEY (driver_id) REFERENCES drivers (id),
    CONSTRAINT fk_auto FOREIGN KEY (auto_id) REFERENCES auto (id)
);

CREATE TABLE driver_licence (
    driver_id INT NOT NULL,
    licence_id INT NOT NULL,
    PRIMARY KEY (driver_id, licence_id),
    CONSTRAINT fk_driverl FOREIGN KEY (driver_id) REFERENCES drivers (id),
    CONSTRAINT fk_licence FOREIGN KEY (licence_id) REFERENCES driving_licence_categories (id)
);

CREATE TABLE users (
  id INT(11) unsigned NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL DEFAULT '',
  email varchar(64) NOT NULL DEFAULT '',
  firstname varchar(64) NOT NULL DEFAULT '',
  lastname varchar(64) NOT NULL DEFAULT '',
  password varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

ALTER TABLE users
  ADD UNIQUE INDEX username_UNIQUE (username ASC),
  ADD UNIQUE INDEX email_UNIQUE (email ASC);

-- inserting values in tables...

INSERT INTO driving_licence_categories
VALUES(NULL, 'A1', 'мопеди, моторолери та інші двоколісні ТЗ, які мають двигун з робочим об’ємом до 50 куб. см або електродвигун потужністю до 4 кВт'),
  (NULL, 'A', 'мотоцикли та інші двоколісні ТЗ, які мають двигун з робочим об’ємом 50 куб. см і більше або електродвигун потужністю 4 кВт і більше'),
  (NULL, 'B1', 'квадро- три- цикли, мотоцикли з боковим причепом та інші 3/4 колісні мототранспортні засоби, дозволена максимальна маса яких < 400кг'),
  (NULL, 'B', 'автомобілі, дозволена максимальна маса яких < 3500 кілограмів (7700 фунтів), кількість сидячих місць 1+8, та причепом < 750кг'),
  (NULL, 'C1', 'призначені для перевезення вантажів автомобілі, дозволена максимальна маса яких становить >3500 <7500кг (>7700 <16500 фунтів), та причепом <750кг'),
  (NULL, 'C', 'призначені для перевезення вантажів автомобілі, дозволена максимальна маса яких >7500кг (16500 фунтів), та причепом <750кг'),
  (NULL, 'D1', 'призначені для перевезення пасажирів автобуси, у яких кількість місць для сидіння, крім сидіння водія, не перевищує 16, та причепом<750кг'),
  (NULL, 'D', 'призначені для перевезення пасажирів автобуси, у яких кількість місць для сидіння, крім сидіння водія, більше 16, та причепом <750кг'),
  (NULL, 'BE', 'B + причеп >750кг'),
  (NULL, 'C1E', 'C1 + причеп >750кг'),
  (NULL, 'CE', 'C + причеп >750кг'),
  (NULL, 'DE', 'D + причеп >750кг'),
  (NULL, 'T', 'трамваї та тролейбуси');

INSERT INTO auto_manufacturer VALUES(null, 'SCANIA', 'Best trucks & good sounding engines!');
INSERT INTO auto_manufacturer VALUES(null, 'VOLVO', 'other sweden truck manufacturer');
INSERT INTO auto_manufacturer VALUES(null, 'MAN', 'Truck manufacturer from Germany');

INSERT INTO auto VALUES(null, 1, 'R730LA6X4MNA', 'YS2R6X400C2078249', 11, 'V8 DC16', 730, 6, 'Scania Opticruise 12+2', '6x4 timber truck', 75000, 10000, 'BX6673BI', 'info');
