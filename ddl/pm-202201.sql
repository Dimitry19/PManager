drop table IF EXISTS  ADMINISTRATOR;
create table ADMINISTRATOR
(
    ID BIGINT auto_increment primary key,
    NAME VARCHAR(255) not null,
    USERNAME VARCHAR(255) not null,
    EMAIL VARCHAR(255) not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    CANCELLED BOOLEAN not null
);

drop table IF EXISTS   AIRLINE;
create table AIRLINE
(
    CODE VARCHAR(255) not null,
    TOKEN VARCHAR(255) not null,
    DESCRIPTION VARCHAR(255),
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    CANCELLED BOOLEAN not null,
    primary key (CODE, TOKEN)
);

create table CITY
(
    ID   VARCHAR not null
        primary key,
    NAME VARCHAR not null
);


drop table IF EXISTS  BATCH_JOB_INSTANCE;
create table BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT   auto_increment
        primary key,
    VERSION BIGINT,
    JOB_NAME VARCHAR(100) not null,
    JOB_KEY VARCHAR(32) not null,
    unique (JOB_NAME, JOB_KEY)
);

drop table IF EXISTS  BATCH_JOB_EXECUTION;
create table BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID BIGINT   auto_increment
        primary key,
    VERSION BIGINT,
    JOB_INSTANCE_ID BIGINT not null,
    CREATE_TIME TIMESTAMP(26,6) not null,
    START_TIME TIMESTAMP(26,6) default NULL,
    END_TIME TIMESTAMP(26,6) default NULL,
    STATUS VARCHAR(10),
    EXIT_CODE VARCHAR(2500),
    EXIT_MESSAGE VARCHAR(2500),
    LAST_UPDATED TIMESTAMP(26,6),
    JOB_CONFIGURATION_LOCATION VARCHAR(2500),
    constraint JOB_INST_EXEC_FK
        foreign key (JOB_INSTANCE_ID) references BATCH_JOB_INSTANCE
);

drop table IF EXISTS  BATCH_JOB_EXECUTION_CONTEXT;
create table BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID BIGINT not null
        primary key,
    SHORT_CONTEXT VARCHAR(2500) not null,
    SERIALIZED_CONTEXT VARCHAR,
    constraint JOB_EXEC_CTX_FK
        foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION
);

drop table IF EXISTS  BATCH_JOB_EXECUTION_PARAMS;
create table BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT not null,
    TYPE_CD VARCHAR(6) not null,
    KEY_NAME VARCHAR(100) not null,
    STRING_VAL VARCHAR(250),
    DATE_VAL TIMESTAMP(26,6) default NULL,
    LONG_VAL BIGINT,
    DOUBLE_VAL DOUBLE,
    IDENTIFYING CHAR(1) not null,
    constraint JOB_EXEC_PARAMS_FK
        foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION
);

drop table IF EXISTS  BATCH_STEP_EXECUTION;
create table BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID BIGINT   auto_increment
        primary key,
    VERSION BIGINT not null,
    STEP_NAME VARCHAR(100) not null,
    JOB_EXECUTION_ID BIGINT not null,
    START_TIME TIMESTAMP(26,6) not null,
    END_TIME TIMESTAMP(26,6) default NULL,
    STATUS VARCHAR(10),
    COMMIT_COUNT BIGINT,
    READ_COUNT BIGINT,
    FILTER_COUNT BIGINT,
    WRITE_COUNT BIGINT,
    READ_SKIP_COUNT BIGINT,
    WRITE_SKIP_COUNT BIGINT,
    PROCESS_SKIP_COUNT BIGINT,
    ROLLBACK_COUNT BIGINT,
    EXIT_CODE VARCHAR(2500),
    EXIT_MESSAGE VARCHAR(2500),
    LAST_UPDATED TIMESTAMP(26,6),
    constraint JOB_EXEC_STEP_FK
        foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION
);

drop table IF EXISTS  BATCH_STEP_EXECUTION_CONTEXT;
create table BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID BIGINT not null
        primary key,
    SHORT_CONTEXT VARCHAR(2500) not null,
    SERIALIZED_CONTEXT VARCHAR,
    constraint STEP_EXEC_CTX_FK
        foreign key (STEP_EXECUTION_ID) references BATCH_STEP_EXECUTION
);

drop table IF EXISTS  CATEGORY;
create table CATEGORY
(
    CODE VARCHAR(15) not null
        primary key,
    DESCRIPTION VARCHAR(255) not null
);

drop table IF EXISTS  COMMUNICATION;
create table COMMUNICATION
(
    ID BIGINT  auto_increment
        primary key,
    TYPE VARCHAR(255) not null,
    R_ADMIN_ID BIGINT,
    CONTENT VARCHAR(255) not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    CANCELLED BOOLEAN not null,
    constraint FKH9HRBGRAKSHF04DBAUJ1IYWEL
        foreign key (R_ADMIN_ID) references ADMINISTRATOR
);

drop table IF EXISTS  CONTACT_US;
create table CONTACT_US
(
    ID BIGINT auto_increment
        primary key,
    SENDER VARCHAR(255) not null,
    RECEIVER VARCHAR(255) not null,
    SUBJECT VARCHAR(255) not null,
    PSEUDO_SENDER VARCHAR(255),
    CONTENT VARCHAR(500) not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6)
);

drop table IF EXISTS  IMAGE;
create table IMAGE
(
    ID BIGINT   auto_increment  primary key,
    TYPE VARCHAR(255),
    ORIGIN VARCHAR(255),
    NAME VARCHAR(255) not null  unique,
    PIC_BYTE binary,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6)
);

drop table IF EXISTS  NOTIFICATION;
create table NOTIFICATION
(
    ID BIGINT   auto_increment
        primary key,
    TITLE VARCHAR(60) not null,
    MESSAGE VARCHAR(255) not null,
    TYPE VARCHAR(15) not null,
    STATUS VARCHAR(10) not null,
    USER_ID BIGINT not null,
    R_ANNOUNCE_ID BIGINT,
    R_USER_ID BIGINT,
    SESSION_ID VARCHAR(255),
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6)
);

drop table IF EXISTS  ROLE;
create table ROLE
(
    ID INTEGER   auto_increment
        primary key,
    DESCRIPTION VARCHAR(255) not null
);

drop table IF EXISTS  TP_USER;
create table TP_USER
(
    ID BIGINT   auto_increment
        primary key,

    LAST_NAME VARCHAR(255) not null,
    FIRST_NAME VARCHAR(255) not null,
    GENDER VARCHAR(10),
    PHONE VARCHAR(35) not null,
    EMAIL VARCHAR(255) not null unique,
    USERNAME VARCHAR(15) not null  unique,
    PASSWORD VARCHAR(255) not null,
    ACTIVE INTEGER not null,
    ENABLE_NOTIF BOOLEAN not null ,
    CONFIRM_TOKEN VARCHAR(255),
    FACEBOOK_ID VARCHAR(255),
    GOOGLE_ID VARCHAR(255),
    IMAGE_ID BIGINT,
    CANCELLED BOOLEAN not null,
    ERROR VARCHAR(255),
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    unique (EMAIL, USERNAME),
    constraint FKPKUUKDHP1VI4PQWEMNYGTNO1T
        foreign key (IMAGE_ID) references IMAGE
);

drop table IF EXISTS  ANNOUNCE;
create table ANNOUNCE
(
    ID BIGINT  auto_increment
        primary key,
    TOKEN VARCHAR(255) not null,
    DEPARTURE VARCHAR(255) not null,
    ARRIVAL VARCHAR(255) not null,
    START_DATE TIMESTAMP(26,6) not null,
    END_DATE TIMESTAMP(26,6) not null,
    DESCRIPTION VARCHAR(255) not null,
    TRANSPORT VARCHAR(255) not null,
    ANNOUNCE_TYPE VARCHAR(10),
    WEIGHT DECIMAL(19,2) not null,
    REMAIN_WEIGHT DECIMAL(19,2),
    PRICE DECIMAL(19,2) not null,
    GOLD_PRICE DECIMAL(19,2) not null,
    PRENIUM_PRICE DECIMAL(19,2) not null,
    STATUS VARCHAR(10),
    --COUNTTER_RESERVATION INTEGER,
    R_USER_ID BIGINT,
    IMAGE_ID BIGINT,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    constraint FKFW0TKPEM5N1S5HA75UD6QJMQ
        foreign key (R_USER_ID) references TP_USER,
    constraint FKP72D81HLBK413NI11Y1D57RHQ
        foreign key (IMAGE_ID) references IMAGE
);

create unique index PRIMARY_KEY_B9
    on ANNOUNCE (ID);

drop table IF EXISTS  ANNOUNCE_CATEGORY;
create table ANNOUNCE_CATEGORY
(
    ANNOUNCE_ID BIGINT not null,
    CATEGORIES_CODE VARCHAR(15) not null,
    primary key (ANNOUNCE_ID, CATEGORIES_CODE),
    constraint FKDLW4JG18D18NXMEQB8AHFXRWF
        foreign key (ANNOUNCE_ID) references ANNOUNCE,
    constraint FKQ084C7R4NIQ9HTWTJS3XQSQFF
        foreign key (CATEGORIES_CODE) references CATEGORY
);

drop table IF EXISTS  MESSAGE;
create table MESSAGE
(
    ID BIGINT not null,
    TOKEN VARCHAR(255) not null,
    USERNAME VARCHAR(255),
    CONTENT VARCHAR(255),
    R_ANNOUNCE BIGINT,
    R_USER_ID BIGINT,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    primary key (ID, TOKEN),
    constraint FKE39G9PLVG5K7X0GDURX5IIDLK
        foreign key (R_ANNOUNCE) references ANNOUNCE,
    constraint FKOGVI3LF5JH16WQIAXRRPJSXMA
        foreign key (R_USER_ID) references TP_USER
);

drop table IF EXISTS  RESERVATION;
create table RESERVATION
(
    ID BIGINT   auto_increment
        primary key,
    DESCRIPTION VARCHAR(255),
    WEIGTH DECIMAL(19,2) not null,
    R_ANNOUNCE_ID BIGINT,
    R_USER_ID BIGINT,
    VALIDATE VARCHAR(10) default 'INSERTED',
    STATUS VARCHAR(10),
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    constraint FK3H0SO8JJDQDGNN9PY00I83OFU
        foreign key (R_ANNOUNCE_ID) references ANNOUNCE,
    constraint FKTCN6F6DKS6UF7KRCMVV2KHAU0
        foreign key (R_USER_ID) references TP_USER
);

drop table IF EXISTS  RESERVATION_CATEGORY;
create table RESERVATION_CATEGORY
(
    RESERVATION_ID BIGINT not null,
    CATEGORIES_CODE VARCHAR(15) not null,
    primary key (RESERVATION_ID, CATEGORIES_CODE),
    constraint FKGCK7KFDQS1IFHMYB24PP251JL
        foreign key (RESERVATION_ID) references RESERVATION,
    constraint FKHI8HYAJ537D5OQOK2XA9UAIN1
        foreign key (CATEGORIES_CODE) references CATEGORY
);

drop table IF EXISTS  REVIEW;
create table REVIEW
(
    ID BIGINT   auto_increment,
    TOKEN VARCHAR(5) not null,
    TITLE VARCHAR(35) not null,
    DETAILS VARCHAR(5000) not null,
    RATING INTEGER not null,
    INDEXES INTEGER not null,
    R_USER_ID BIGINT not null,
    RATING_USER_ID BIGINT,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP(26,6),
    LASTUPDATED TIMESTAMP(26,6),
    constraint FKMN7YQ16MASQJ4ODIT0TOHL3XS
        foreign key (R_USER_ID) references TP_USER
);

drop table IF EXISTS  SUBSCRIBERS;
create table SUBSCRIBERS
(
    R_USER_ID BIGINT not null,
    SUBSCRIBER_ID BIGINT not null,
    primary key (R_USER_ID, SUBSCRIBER_ID),
    constraint FK1NVJXHQAUIACV41MVBPO4R6IU
        foreign key (R_USER_ID) references TP_USER,
    constraint FKOGEX3X8F6IE4WW40KGOI8O98U
        foreign key (SUBSCRIBER_ID) references TP_USER
);

drop table IF EXISTS  SUBSCRIPTIONS;
create table SUBSCRIPTIONS
(
    SUBSCRIPTION_ID BIGINT not null,
    R_USER_ID BIGINT not null,
    primary key (SUBSCRIPTION_ID, R_USER_ID),
    constraint FK5MGUIDOAT1GBMLUK1NV9VHJKD
        foreign key (R_USER_ID) references TP_USER,
    constraint FKG9EHR2OF3WIDR0N9R03M5U6SA
        foreign key (SUBSCRIPTION_ID) references TP_USER
);

create unique index PRIMARY_KEY_273
    on TP_USER (ID);

drop table IF EXISTS  TP_USER_COMMUNICATION;
create table TP_USER_COMMUNICATION
(
    USERS_ID BIGINT not null,
    COMMUNICATIONS_ID BIGINT not null,
    primary key (USERS_ID, COMMUNICATIONS_ID),
    constraint FKAACENEGVR45D01M491GVVB7XE
        foreign key (USERS_ID) references TP_USER,
    constraint FKPPG2OD2BISCSJQOY2F9EU0YHP
        foreign key (COMMUNICATIONS_ID) references COMMUNICATION
);

drop table IF EXISTS  USER_NOTIFICATION;
create table USER_NOTIFICATION
(
    USERS_ID BIGINT not null,
    NOTIFICATIONS_ID BIGINT not null,
    primary key (USERS_ID, NOTIFICATIONS_ID),
    constraint FK8HR3CBMBTB3NDL456F6BASWMN
        foreign key (NOTIFICATIONS_ID) references NOTIFICATION,
    constraint FKO1QX6Y02V6L3UXNG68RXJD9GR
        foreign key (USERS_ID) references TP_USER
);

drop table IF EXISTS  USER_ROLE;
create table USER_ROLE
(
    R_USER BIGINT not null,
    ROLE_ID INTEGER not null,
    primary key (R_USER, ROLE_ID),
    constraint FKH3WNT7IJXYT7F1JS45H8W3FOH
        foreign key (R_USER) references TP_USER,
    constraint FKN1RN9QODD3U4LE8UF3KL33QE3
        foreign key (ROLE_ID) references ROLE
);

