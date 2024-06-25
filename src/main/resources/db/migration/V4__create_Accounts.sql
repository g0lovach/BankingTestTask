create table if not exists Accounts
(
    id bigint primary key generated always as identity ,
    user_id bigint not null ,
    division_id bigint not null,
    currency_id int not null ,
    acc_number varchar(20) unique ,
    cor_acc varchar(20) not null,
    balance numeric(21,2) not null ,
    time_open timestamp not null ,
    time_close timestamp,
    FOREIGN KEY (user_id) references Users(id),
    FOREIGN KEY (division_id) references Divisions(id),
    FOREIGN KEY (currency_id) references Currencies(id)
);
