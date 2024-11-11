--	public WireType(String code, String description, int speed) 
create table if not exists RCG_Wire_type(
	code varchar,
	description varchar NOT NULL,
	speed smallint NOT NULL CHECK (speed > 0),
	
	constraint pk_RCG_wiretype primary key (code)
);
