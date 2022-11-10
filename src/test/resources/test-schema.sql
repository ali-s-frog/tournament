create table tournament (id serial not null primary key, name varchar(255) not null, create_date timestamp not null, updated_at timestamp not null);
create table team (id serial not null primary key, name varchar(255) not null, create_date timestamp not null, updated_at timestamp not null);
create table tournament_team (tournament_id integer not null, team_id integer not null);
insert into tournament (name, create_date, updated_at) values ('Turkish Super League', now(), now()),('UEFA Champions League', now(), now());
insert into team (name, create_date, updated_at) values ('Galatasaray', now(), now());
