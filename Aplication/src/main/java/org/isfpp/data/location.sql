--public Location(String code, String description)
create table if not exists RCG_Location(
	code varchar,
	description varchar not null,
	
	constraint pk_RCG_location primary key (code)
);