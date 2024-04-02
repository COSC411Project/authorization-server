create table token (
	id INTEGER NOT NULL Primary Key AUTO_INCREMENT,
    client_id INTEGER NOT NULL,
    authorization_code_id INTEGER,
	token varchar(600),
    datetime_issued datetime NOT NULL,
    expires_in INTEGER NOT NULL,
    scope varchar(15),
    valid boolean NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id),
    FOREIGN KEY (authorization_code_id) REFERENCES authorization_code(id)
);