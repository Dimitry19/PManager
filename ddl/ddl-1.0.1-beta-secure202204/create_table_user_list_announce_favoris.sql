-- Recuperer le script de creation de la table H2 ou MySql
create table user_announces_favoris
(
    USER_ID     BIGINT not null,
    ANNOUNCE_ID BIGINT not null,
    primary key (USER_ID, ANNOUNCE_ID),
    constraint FK1Y6CLURE4R2YJMN2TX8ERSXR8
        foreign key (USER_ID) references TP_USER (ID),
    constraint FKF3UV1T15EIKMGVASQJMQ4AXLC
        foreign key (ANNOUNCE_ID) references ANNOUNCE (ID)
);