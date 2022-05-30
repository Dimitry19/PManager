-- Recuperer le script de creation de la table H2 ou MySql
-- auto-generated definition
create table PRICING
(
    CODE        VARCHAR(10)    not null
        constraint UK_T9XMR1J134GUU76H39O9I2R2J
            unique,
    TOKEN       VARCHAR(10)    not null,
    PRICE DECIMAL(19,2) not null,
    CANCELLED   BOOLEAN        not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP
);
create unique index PRICING_PRICE_UINDEX
    on PRICING (PRICE);


create table SUBSCRIPTION_PRICING
(
    CODE VARCHAR(10) not null
        constraint UK_5WHEFM9QUU8FAS8GWNUGMF62N
            unique,
    TOKEN VARCHAR(10) not null,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    DESCRIPTION VARCHAR(255) not null,
    END_DATE TIMESTAMP not null,
    START_DATE TIMESTAMP not null,
    TYPE VARCHAR(10) not null,
    R_PRICING_CODE VARCHAR(10),
    R_PRICING_TOKEN VARCHAR(10),
    constraint FKPSP4KF7RYUQ6POS6CQW713D0C
        foreign key (R_PRICING_CODE, R_PRICING_TOKEN) references PRICING (CODE, TOKEN)
);

