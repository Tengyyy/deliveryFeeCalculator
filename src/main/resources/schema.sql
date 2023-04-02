create table weather(
    id int primary key auto_increment,
    station_name varchar(100),
    wmo_code int,
    air_temp float,
    wind_speed float,
    weather_phenomenon varchar(100) null,
    observation_time timestamp
);