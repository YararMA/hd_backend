alter table if exists user_role
drop
constraint if exists user_role_user_fk;
drop table if exists user_role cascade;
drop table if exists usr cascade;

create table user_role
(
    user_id bigint not null,
    roles   varchar(255)
);

create table usr
(
    id               int8 GENERATED BY DEFAULT AS IDENTITY,
    name             varchar(100)        NOT NULL,
    firstname        varchar(100)        NOT NULL,
    lastname         varchar(100),
    username         varchar(100) UNIQUE NOT NULL,
    phone            varchar(100),
    gender           varchar(50),
    birthday         date,
    country          varchar(255),
    region           varchar(255),
    locality         varchar(255),
    type_of_activity varchar(100),
    password         varchar(255)        NOT NULL,
    organization_id  int8,
    active           boolean default true,
    primary key (id)
);

create index idx_usr_username on usr (username);

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;