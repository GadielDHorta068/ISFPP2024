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