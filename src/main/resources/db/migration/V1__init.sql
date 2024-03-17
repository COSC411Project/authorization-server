create table user (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	email varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	role varchar(15) NOT NULL,
	enabled BOOLEAN NOT NULL
);

create table client (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	identifier varchar(50) NOT NULL,
	secret varchar(100) NOT NULL,
	requires_consent BOOLEAN NOT NULL
);

create table client_grant_type (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	client_id INTEGER NOT NULL,
	grant_type varchar(25) NOT NULL,
	FOREIGN KEY (client_id) REFERENCES client(id)
);

create table client_scope (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	client_id INTEGER NOT NULL,
	scope varchar(25) NOT NULL,
	FOREIGN KEY (client_id) REFERENCES client(id)
);