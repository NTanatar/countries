
INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('Italia', 'Europe', 'Unitary parliamentary constitutional republic', 20, false, null);
INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('United Kingdom', 'Europe', 'Unitary parliamentary constitutional monarchy', 4, false, null);
INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('Slovenia', 'Europe', 'Unitary parliamentary constitutional republic', 12, true, '1918-10-29');
INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('Japan', 'East Asia', 'Unitary dominant-party parliamentary constitutional monarchy', 8, false, null);
INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('Canada', 'North America', 'Federal parliamentary constitutional monarchy', 13, false, '1867-07-01');
INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('Israel', 'West Asia', 'Unitary parliamentary constitutional republic', 6, false, '1948-05-14');
INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('Australia', 'Oceania', 'Federal parliamentary constitutional monarchy', 6, false, null);

insert into cities (name, country_id, founding_date, city_day, has_river, population)
values ('Florence', 1, null, null, true, 383083);
insert into cities (name, country_id, founding_date, city_day, has_river, population)
values ('York', 2, null, null, true, 210618);
insert into cities (name, country_id, founding_date, city_day, has_river, population)
values ('Kyoto', 4, null, null, true, 1468980);
insert into cities (name, country_id, founding_date, city_day, has_river, population)
values ('Montreal', 5, null, null, true, 1704693);
insert into cities (name, country_id, founding_date, city_day, has_river, population)
values ('Tel Aviv', 6, '1909-04-11', null, true,  460613);
insert into cities (name, country_id, founding_date, city_day, has_river, population)
values ('Melbourne', 7, '1835-08-30', null, true, 4529500);
insert into cities (name, country_id, founding_date, city_day, has_river, population)
values ('Liverpool', 2, '1207-08-28', '1207-08-28', true, 498042);

/* update  */
update cities set founding_date = '1942-05-17', population = 1704694 where name = 'Montreal';

/* select */

select name from countries where landlocked = true;
select world_region from countries where government_type like '%monarchy%';
select name, world_region from countries where founding_date is not null;

select ci.name from cities ci join countries co on country_id = co.id where world_region = 'West Asia';
select ci.name, co.name from cities ci join countries co on country_id = co.id where population > 800000;

/* insert and delete */

INSERT INTO countries (name, world_region, government_type, regions_count, landlocked, founding_date)
VALUES ('North Korea', 'East Asia', 'Unitary Juche one-party republic under a totalitarian dictatorship', 9, false, null);

delete from countries where government_type like '%dictatorship%'