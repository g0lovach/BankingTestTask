create table if not exists Currencies
(
    id int primary key generated always as identity ,
    name VARCHAR(3) not null unique ,
    code VARCHAR(3) not null unique
) ;

insert into Currencies(name,code)
values ('RUB','810'),
       ('USD','840'),
       ('EUR','978')
