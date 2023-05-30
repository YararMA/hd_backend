drop table if exists organization cascade;

create table organization
(
    id          int8 GENERATED BY DEFAULT AS IDENTITY,
    name        varchar(255) UNIQUE NOT NULL,
    description varchar(1000)       NOT NULL,
    active      boolean default false,
    user_id int8 UNIQUE,
    primary key (id)
);