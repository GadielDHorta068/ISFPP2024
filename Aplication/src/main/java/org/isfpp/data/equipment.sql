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


-- puertos del equipo(PortType portType, Equt equipment)
create table if not exists RCG_equipmentipmen_port(
cantidad smallint NOT NULL CHECK (cantidad > 0),
code_port_type varchar,
code_equipment varchar,
constraint fk_port_equipo foreign key (code_equipment) references rcg_equipment(code),
constraint fk_port_portype foreign key (code_portype) references rcg_portype(code)
);

--tabla de ip del equipo
CREATE TABLE IF NOT EXISTS RCG_equipment_ips (
    code_equipment VARCHAR NOT NULL,
    ip INET NOT NULL CHECK (position(':' IN text(ip)) > 0)
);

CREATE INDEX ip_id_idx ON rcg_ips(ip)
