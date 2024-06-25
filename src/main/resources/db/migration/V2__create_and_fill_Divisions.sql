create table if not exists Divisions
(
    id bigint primary key generated always as identity ,
    name VARCHAR not null unique,
    code varchar(4) not null unique,
    bic varchar(9) not null unique,
    inn varchar(10) not null unique
);

insert into Divisions(name, code, bic, inn)
values ('Отделение 1 ПАО NБанк','1111','123456780', '7700000000'),
       ('Отделение 2 ПАО NБанк','1112','123456781', '7700000001'),
       ('Отделение 1 ПАО MБанк','1113','123456782', '7700000002')


