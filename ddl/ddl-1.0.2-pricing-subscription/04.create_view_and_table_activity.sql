--H2 Version
DROP VIEW  IF EXISTS VIEWS.v_user_personal_data;

CREATE  VIEW VIEWS.v_user_personal_data AS
SELECT U.ID,
       FIRST_NAME,
       LAST_NAME,
       USERNAME,
       GENDER,
       COUNTRY,CITY,
       I.ORIGIN,
       I.PIC_BYTE,
       PHONE,
       EMAIL
FROM TP_USER U
    LEFT JOIN IMAGE I on U.IMAGE_ID = I.ID
WHERE U.CANCELLED IS FALSE AND U.ACTIVE=1;

create table ACTIVITY
(
    CODE        VARCHAR(5)   not null,
    TOKEN       VARCHAR(255) not null,
    ACTIVITY    VARCHAR(255) not null,
    OPERATION   VARCHAR(10),
    R_USER_ID   BIGINT       not null,
    CANCELLED   BOOLEAN      not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    primary key (CODE, TOKEN)
);

create table ACTIVITIES_USER_PERSONAL_DATA
(
    CODE    VARCHAR(5)   not null,
    TOKEN   VARCHAR(255) not null,
    USER_ID BIGINT       not null,
    primary key (CODE, TOKEN, USER_ID),
    constraint FK8TDKRATBME3G7RO07AYISKVT5
        foreign key (CODE, TOKEN) references ACTIVITY (CODE, TOKEN)
);

--MySql Version

DROP VIEW  IF EXISTS v_user_personal_data;

CREATE  VIEW v_user_personal_data AS
SELECT U.ID,
       FIRST_NAME,
       LAST_NAME,
       USERNAME,
       GENDER,
       COUNTRY,CITY,
       I.ORIGIN,
       I.PIC_BYTE,
       PHONE,
       EMAIL
FROM TP_USER U
         LEFT JOIN IMAGE I on U.IMAGE_ID = I.ID
WHERE U.CANCELLED IS FALSE AND U.ACTIVE=1;