create table post(
	id serial primary key unique,
	name varchar not null,
	text text not null,
	link varchar not null unique,
	created date not null
);