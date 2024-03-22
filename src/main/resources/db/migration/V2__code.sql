create table authorization_code (
	id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	client_id INTEGER NOT NULL,
	code varchar(100) NOT NULL,
	redirect_uri varchar(100) NOT NULL,
	datetime_issued datetime NOT NULL,
	used boolean NOT NULL,
	FOREIGN KEY (client_id) references client(id)
)
