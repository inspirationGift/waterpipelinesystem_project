-- CREATE SCHEMA if not exists MY_SCHEMA;
-- SET SCHEMA MY_SCHEMA;

DROP TABLE if exists WaterPipeLineSystem;
DROP TABLE if exists route;


create table WaterPipeLineSystem
(
    id     int NOT NULL primary key auto_increment,
    "idX"  int not null,
    "idY"  int not null,
    length int not null
);

create table route
(
    id    int NOT NULL primary key AUTO_INCREMENT,
    "idA" int NOT NULL,
    "idB" int NOT NULL
);

create table result
(
    id     int     NOT NULL,
    exist  boolean NOT NULL,
    length int     NOT NULL
);


