set schema 'poo2024';

--String code, String description, int speed) 
create table if not exists RCG_port_Type(
	code varchar,
	description varchar NOT NULL,
	speed smallint NOT NULL CHECK (speed > 0),
	
	constraint pk_RCG_portType primary key (code)
);

--public Location(String code, String description)
create table if not exists RCG_Location(
	code varchar,
	description varchar not null,
	
	constraint pk_RCG_location primary key (code)
);

--	public WireType(String code, String description, int speed) 
create table if not exists RCG_Wire_type(
	code varchar,
	description varchar NOT NULL,
	speed smallint NOT NULL CHECK (speed > 0),
	
	constraint pk_RCG_wiretype primary key (code)
);

-- tipos de equipos(String code, String description) 
create table if not exists rcg_equipment_type(
	code varchar NOT NULL,
	description varchar NOT NULL,
	
	constraint pk_RCG_equipment_type primary key (code)
);

--	public Connection(Port port1, Port port2, WireType wire)
create table if not exists rcg_connection( 
	ID_calificacion SERIAL PRIMARY KEY,
	code_port_type1 varchar,
	code_equipment1 varchar,
	code_port_type2 varchar,
	code_equipment2 varchar,
	code_wire_type VARCHAR,
	
	constraint fk_rcg_connection_wire foreign key (code_wire_type) references rcg_wire_Type(code),
	constraint fk_rcg_connection_port_type1 foreign key (code_port_type1) references rcg_port_type(code),
	constraint fk_rcg_connection_equipment1 foreign key (code_equipment1) references rcg_equipment(code),
	constraint fk_rcg_connection_port_type2 foreign key (code_port_type2) references rcg_port_type(code),
	constraint fk_rcg_connection_equipment2 foreign key (code_equipment2) references rcg_equipment(code)
);



--	public Equipment addEquipment(String code, String description, 
--String marca, String model, PortType portType,int cantidad,
-- EquipmentType equipmentType, Location location,Boolean status) 
CREATE TABLE IF NOT EXISTS RCG_equipment(
	code Varchar,
	description varchar NOT NULL,
	marca varchar  NOT NULL,
	modelo varchar NOT NULL,
	code_location varchar NOT NULL,
	code_equipment_type varchar NOT NULL,
	status boolean,
	
	constraint PK_RCG_equipos primary key (code),
	CONSTRAINT fK_RCG_equipment_location FOREIGN KEY (code_location) REFERENCES rcg_location(code),
	CONSTRAINT fk_RCG_equipment_equipment_type FOREIGN KEY (code_equipment_type) REFERENCES rcg_equipment_type(code) 
);


-- puertos del equipo(PortType portType, Equipment equipment) 
create table if not exists RCG_equipment_port(
	cantidad smallint NOT NULL CHECK (cantidad > 0),
	code_portype varchar,
	code_equipment varchar,
	
	constraint fk_port_equipo foreign key (code_equipment) references rcg_equipment(code),
	constraint fk_port_port_type foreign key (code_portype) references rcg_port_type(code)
);

--tabla de ip del equipo
CREATE TABLE IF NOT EXISTS RCG_equipment_ips (
    code_equipment VARCHAR NOT NULL,
    ip INET NOT NULL CHECK (position(':' IN text(ip)) > 0)
);

CREATE INDEX ip_id_idx ON rcg_ips(ip)

select * from  rcg_ips

