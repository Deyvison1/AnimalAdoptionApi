-- DROP SCHEMA animal_adoption;

CREATE SCHEMA IF NOT EXISTS animal_adoption AUTHORIZATION postgres;
-- animal_adoption.animal_type definição

-- Drop table

-- DROP TABLE animal_adoption.animal_type;

CREATE TABLE animal_adoption.animal_type (
	creation_Date timestamp(6),
    update_date timestamp(6),
    creation_user varchar(255),
    update_user varchar(255),
	id uuid NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT animal_type_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE animal_adoption.animal_type OWNER TO postgres;
GRANT ALL ON TABLE animal_adoption.animal_type TO postgres;


-- animal_adoption.breed definição

-- Drop table

-- DROP TABLE animal_adoption.breed;

CREATE TABLE animal_adoption.breed (
    creation_Date timestamp(6),
    update_date timestamp(6),
    creation_user varchar(255),
    update_user varchar(255),
	animal_type_id uuid NOT NULL,
	id uuid NOT NULL,
	"name" varchar(255) NULL,
	nationality varchar(255) NULL,
	CONSTRAINT breed_pkey PRIMARY KEY (id),
	CONSTRAINT fkr0i0u3b252pay42ktbgxf19st FOREIGN KEY (animal_type_id) REFERENCES animal_adoption.animal_type(id)
);

-- Permissions

ALTER TABLE animal_adoption.breed OWNER TO postgres;
GRANT ALL ON TABLE animal_adoption.breed TO postgres;


-- animal_adoption.animal definição

-- Drop table

-- DROP TABLE animal_adoption.animal;

CREATE TABLE animal_adoption.animal (
	age int4 NULL,
	creation_Date timestamp(6),
    update_date timestamp(6),
    creation_user varchar(255),
    update_user varchar(255),
	breed_id uuid NOT NULL,
	id uuid NOT NULL,
	description varchar(255) NULL,
	images text NULL,
	"name" varchar(255) NULL,
	CONSTRAINT animal_pkey PRIMARY KEY (id),
	CONSTRAINT fk5vuppijm6mptl6xm5g9jhegwh FOREIGN KEY (breed_id) REFERENCES animal_adoption.breed(id)
);

-- Permissions

ALTER TABLE animal_adoption.animal OWNER TO postgres;
GRANT ALL ON TABLE animal_adoption.animal TO postgres;


-- animal_adoption.cat definição

-- Drop table

-- DROP TABLE animal_adoption.cat;

CREATE TABLE animal_adoption.cat (
	available bool NOT NULL,
	id uuid NOT NULL,
	CONSTRAINT cat_pkey PRIMARY KEY (id),
	CONSTRAINT fk53w1892tbomay6480732f094k FOREIGN KEY (id) REFERENCES animal_adoption.animal(id)
);

-- Permissions

ALTER TABLE animal_adoption.cat OWNER TO postgres;
GRANT ALL ON TABLE animal_adoption.cat TO postgres;


-- animal_adoption.contact definição

-- Drop table

-- DROP TABLE animal_adoption.contact;

CREATE TABLE animal_adoption.contact (
    creation_Date timestamp(6),
    update_date timestamp(6),
    creation_user varchar(255),
    update_user varchar(255),
	animal_id uuid NULL,
	id uuid NOT NULL,
	"name" varchar(255) NULL,
	value varchar(255) NULL,
	CONSTRAINT contact_pkey PRIMARY KEY (id),
	CONSTRAINT fkskmxpjk6dk0s9f1k9tu8iwuo5 FOREIGN KEY (animal_id) REFERENCES animal_adoption.animal(id)
);

-- Permissions

ALTER TABLE animal_adoption.contact OWNER TO postgres;
GRANT ALL ON TABLE animal_adoption.contact TO postgres;


-- animal_adoption.dog definição

-- Drop table

-- DROP TABLE animal_adoption.dog;

CREATE TABLE animal_adoption.dog (
	available bool NULL,
	id uuid NOT NULL,
	CONSTRAINT dog_pkey PRIMARY KEY (id),
	CONSTRAINT fken2uprx2ikq0tl0jstjxhef8q FOREIGN KEY (id) REFERENCES animal_adoption.animal(id)
);

-- Permissions

ALTER TABLE animal_adoption.dog OWNER TO postgres;
GRANT ALL ON TABLE animal_adoption.dog TO postgres;




-- Permissions

GRANT ALL ON SCHEMA animal_adoption TO postgres;