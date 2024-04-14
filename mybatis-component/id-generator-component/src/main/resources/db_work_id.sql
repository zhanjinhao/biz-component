-- auto-generated definition
create table t_snow_flake_worker_id
(
    id       int auto_increment
        primary key,
    next_id  int         not null,
    app_name varchar(50) not null,
    constraint t_snow_flake_worker_id_app_name_uindex
        unique (app_name)
);

