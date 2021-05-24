create table post(
	id serial primary key,
	name varchar not null,
	text text not null,
	link varchar not null unique,
	created timestamp not null
);