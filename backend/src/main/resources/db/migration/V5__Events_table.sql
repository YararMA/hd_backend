drop table if exists event cascade;

create table event(
  id int8 GENERATED BY DEFAULT AS IDENTITY ,
  name varchar(255),
  description varchar(1000),
  date date,
  PRIMARY KEY (id)
);