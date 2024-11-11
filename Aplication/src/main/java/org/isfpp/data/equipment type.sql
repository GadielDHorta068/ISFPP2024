-- tipos de equipos(String code, String description) 
create table if not exists rcg_equipment_type(
	code varchar NOT NULL,
	description varchar NOT NULL,
	
	constraint pk_RCG_equipment_type primary key (code)
);