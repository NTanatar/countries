create table if not exists countries
(
    id bigserial not null
        constraint countries_pk
        primary key,
    name varchar,
    world_region varchar,
    government varchar,
    regions_count integer,
    landlocked boolean,
    founding_date date
);

create table if not exists cities
(
    id bigserial not null
        constraint cities_pk
        primary key,
    country_id bigint not null
        constraint cities_countries_id_fk
        references countries,
    name varchar,
    founding_date date,
    city_day date,
    has_river boolean,
    population integer
);
