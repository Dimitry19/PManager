-- Recuperer le script de creation de la table H2 ou MySql
create table user_announces_favoris
(
    USER_ID     bigint not null,
    ANNOUNCE_ID bigint not null,
    primary key (USER_ID, ANNOUNCE_ID),
    constraint FK1y6clure4r2yjmn2tx8ersxr8
        foreign key (USER_ID) references tp_user (ID),
    constraint FKf3uv1t15eikmgvasqjmq4axlc
        foreign key (ANNOUNCE_ID) references announce (ID)
);