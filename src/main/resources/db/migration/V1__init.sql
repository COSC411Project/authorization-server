create table user (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username varchar(100),
	email varchar(100),
	password varchar(100),
    auth_provider varchar(20),
	role varchar(15) NOT NULL,
	enabled BOOLEAN NOT NULL,
    date_created date NOT NULL
);

create table client (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    application_name varchar(50) UNIQUE NOT NULL,
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

create table client_redirect_uri (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    client_id INTEGER NOT NULL,
    redirect_uri varchar(50) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id)
);