create table if not exists Users
(
    id bigint primary key generated always as identity ,
    fio varchar not null
);

insert into Users(fio)
values ('Иванов Иван Иванович'),
       ('Васильев Василий Васильевич'),
       ('Моррисон Джеймс Дугласович')

