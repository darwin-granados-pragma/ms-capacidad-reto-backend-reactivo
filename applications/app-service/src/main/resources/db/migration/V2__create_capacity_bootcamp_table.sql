CREATE TABLE capacidad_bootcamp (
  id_capacidad_bootcamp VARCHAR(50) PRIMARY KEY,
  id_bootcamp VARCHAR(50) NOT NULL,
  id_capacidad VARCHAR(50) NOT NULL,
  FOREIGN KEY (id_capacidad) REFERENCES capacidad(id_capacidad)
);
