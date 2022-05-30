-- Recuperer le script de creation de la table H2 ou MySql
-- auto-generated definition
create table PRICING
(
    CODE        VARCHAR(10)    not null
        constraint UK_T9XMR1J134GUU76H39O9I2R2J
            unique,
    TOKEN       VARCHAR(10)    not null,
    PRICE       DECIMAL(19, 2) not null,
    CANCELLED   BOOLEAN        not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP
);

-- auto-generated definition
create table PRICING_SUBSCRIPTION
(
    CODE            VARCHAR(10)  not null
        constraint UK_4UEGN3MNXQ1LMFV8UU10HTWSI
            unique,
    TOKEN           VARCHAR(10)  not null,
    DESCRIPTION     VARCHAR(255) not null,
    TYPE            VARCHAR(10)  not null,
    START_DATE      TIMESTAMP    not null,
    END_DATE        TIMESTAMP    not null,
    R_PRICING_CODE  VARCHAR(10),
    R_PRICING_TOKEN VARCHAR(10),
    CANCELLED       BOOLEAN      not null,
    DATECREATED     TIMESTAMP,
    LASTUPDATED     TIMESTAMP,
    constraint FK4LG4P1U4RVT6EM7N3RH96P67B
        foreign key (R_PRICING_CODE, R_PRICING_TOKEN) references PRICING (CODE, TOKEN)
);

create table TP_USER_PRICING_SUBSCRIPTIONS
(
    R_SUBSCRIPTION_CODE  VARCHAR(10) not null,
    R_SUBSCRIPTION_TOKEN VARCHAR(10) not null,
    R_USER_ID            BIGINT      not null,
    constraint FKH06LFEF41OKSXAWM9QJNBX7QF
        foreign key (R_USER_ID) references TP_USER (ID),
    constraint FKSUBKI4747TSUPBHE7G4HRI01M
        foreign key (R_SUBSCRIPTION_CODE, R_SUBSCRIPTION_TOKEN) references PRICING_SUBSCRIPTION (CODE, TOKEN)
);

