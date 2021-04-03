alter table countries
    add column created timestamp,
    add column modified timestamp,
    add column created_by varchar(20),
    add column modified_by varchar(20);

alter table cities
    add column created timestamp,
    add column modified timestamp,
    add column created_by varchar(20),
    add column modified_by varchar(20);