create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to devops;

create table userentity
(
    id        bigint       not null
        constraint userentity_pkey
            primary key,
    email     varchar(255) not null
        constraint uk_lj9qh3n6xseqmtsy97bmc8bxr
            unique,
    firstname varchar(255) not null,
    lastname  varchar(255) not null
);

create table catentity
(
    id        bigint       not null
        constraint catentity_pkey
            primary key,
    name varchar(255) not null,
    owner bigint not null references userentity(id)
);

alter table userentity
    owner to devops;

