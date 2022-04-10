-- we don't know how to generate database MANAGER (class Database) :(
drop table IF EXISTS   ADMINISTRATOR;
create table ADMINISTRATOR
(
    ID BIGINT auto_increment primary key,
    NAME VARCHAR(255) not null,
    USERNAME VARCHAR(255) not null,
    EMAIL VARCHAR(255) not null,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP
);

drop table IF EXISTS   AIRLINE;
create table AIRLINE
(
    TOKEN VARCHAR(255) not null,
    CODE VARCHAR(255) not null,
    DESCRIPTION VARCHAR(255),
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    CANCELLED BOOLEAN not null,
    primary key(CODE, TOKEN)
);

create table CITY
(
    ID   VARCHAR(50) not null
        primary key,
    NAME VARCHAR(255) not null
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
    CREATE_TIME TIMESTAMP not null,
    START_TIME TIMESTAMP default NULL,
    END_TIME TIMESTAMP default NULL,
    STATUS VARCHAR(10),
    EXIT_CODE VARCHAR(2500),
    EXIT_MESSAGE VARCHAR(2500),
    LAST_UPDATED TIMESTAMP,
    JOB_CONFIGURATION_LOCATION VARCHAR(2500),
    foreign key (JOB_INSTANCE_ID) references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
);

drop table IF EXISTS  BATCH_JOB_EXECUTION_CONTEXT;
create table BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID BIGINT not null
        primary key,
    SHORT_CONTEXT VARCHAR(2500) not null,
    SERIALIZED_CONTEXT VARCHAR(2500),
    constraint JOB_EXEC_CTX_FK
        foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

drop table IF EXISTS  BATCH_JOB_EXECUTION_PARAMS;
create table BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT not null,
    TYPE_CD VARCHAR(6) not null,
    KEY_NAME VARCHAR(100) not null,
    STRING_VAL VARCHAR(250),
    DATE_VAL TIMESTAMP  default NULL,
    LONG_VAL BIGINT,
    DOUBLE_VAL DOUBLE,
    IDENTIFYING CHAR(1) not null,
    foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

drop table IF EXISTS  BATCH_STEP_EXECUTION;
create table BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID BIGINT   auto_increment
        primary key,
    VERSION BIGINT not null,
    STEP_NAME VARCHAR(100) not null,
    JOB_EXECUTION_ID BIGINT not null,
    START_TIME TIMESTAMP not null,
    END_TIME TIMESTAMP  default NULL,
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
    LAST_UPDATED TIMESTAMP,
    foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

drop table IF EXISTS  BATCH_STEP_EXECUTION_CONTEXT;
create table BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID BIGINT not null
        primary key,
    SHORT_CONTEXT VARCHAR(2500) not null,
    SERIALIZED_CONTEXT VARCHAR(2500),
    foreign key (STEP_EXECUTION_ID) references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);


drop table IF EXISTS  CATEGORY;
create table CATEGORY
(
    CODE VARCHAR(15) not null primary key,
    DESCRIPTION VARCHAR(255) not null
);

drop table IF EXISTS  COMMUNICATION;
create table COMMUNICATION
(
    ID BIGINT  auto_increment
        primary key,
    CONTENT VARCHAR(255) not null,
    TYPE VARCHAR(255) not null,
    R_ADMIN_ID BIGINT,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    foreign key (R_ADMIN_ID) references ADMINISTRATOR(ID)
);

drop table IF EXISTS  CONTACT_US;
create table CONTACT_US
(
    ID BIGINT auto_increment
        primary key,
    CONTENT VARCHAR(500) not null,
    RECEIVER VARCHAR(255) not null,
    SENDER VARCHAR(255) not null,
    SUBJECT VARCHAR(255) not null,
    PSEUDO_SENDER VARCHAR(255),
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
);

drop table IF EXISTS  IMAGE;
create table IMAGE
(
    ID BIGINT   auto_increment
        primary key,
    NAME VARCHAR(255) not null
        unique,
    TYPE VARCHAR(255),
    ORIGIN VARCHAR(255),
    PIC_BYTE binary,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
);

drop table IF EXISTS  NOTIFICATION;
create table NOTIFICATION
(
    ID BIGINT   auto_increment
        primary key,
    R_ANNOUNCE_ID BIGINT,
    MESSAGE VARCHAR(255) not null,
    USER_ID BIGINT not null,
    R_USER_ID BIGINT,
    TITLE VARCHAR(60) not null,
    TYPE VARCHAR(15) not null,
    SESSION_ID VARCHAR(255),
    STATUS VARCHAR(10) not null,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
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
    FIRST_NAME VARCHAR(255) not null,
    LAST_NAME VARCHAR(255) not null,
    GENDER VARCHAR(10),
    PHONE VARCHAR(35) not null,
    USERNAME VARCHAR(15) not null unique,
    PASSWORD VARCHAR(255) not null,
    EMAIL VARCHAR(255) not null unique,
    IMAGE_ID BIGINT,
    FACEBOOK_ID VARCHAR(255),
    GOOGLE_ID VARCHAR(255),
    ENABLE_NOTIF BOOLEAN not null ,
    ACTIVE INTEGER not null,
    CONFIRM_TOKEN VARCHAR(255),
    ERROR VARCHAR(255),
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    CANCELLED BOOLEAN not null,
    unique (EMAIL, USERNAME),
    FOREIGN KEY (IMAGE_ID)
    REFERENCES IMAGE(ID)
);

drop table IF EXISTS  ANNOUNCE;
create table ANNOUNCE
(
    ID BIGINT  auto_increment
        primary key,
    TOKEN VARCHAR(255) not null,
    DEPARTURE VARCHAR(255) not null,
    ARRIVAL VARCHAR(255) not null,
    DESCRIPTION VARCHAR(255) not null,
    TRANSPORT VARCHAR(255) not null,
    WEIGHT DECIMAL(19,2) not null,
    REMAIN_WEIGHT DECIMAL(19,2),
    START_DATE TIMESTAMP not null,
    END_DATE TIMESTAMP not null,
    GOLD_PRICE DECIMAL(19,2) not null,
    PRENIUM_PRICE DECIMAL(19,2) not null,
    PRICE DECIMAL(19,2) not null,
    ANNOUNCE_TYPE VARCHAR(10),
    STATUS VARCHAR(10),
    R_USER_ID BIGINT,
    IMAGE_ID BIGINT,
--     COUNTER_RESERVATION INTEGER,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    foreign key (R_USER_ID) references TP_USER(ID),
    foreign key (IMAGE_ID) references IMAGE(ID)
);

create unique index PRIMARY_KEY_B9
    on ANNOUNCE (ID);

drop table IF EXISTS  ANNOUNCE_CATEGORY;
create table ANNOUNCE_CATEGORY
(
    ANNOUNCE_ID BIGINT not null,
    CATEGORIES_CODE VARCHAR(15) not null,
    primary key (ANNOUNCE_ID, CATEGORIES_CODE),
    foreign key (ANNOUNCE_ID) references ANNOUNCE(ID),
    foreign key (CATEGORIES_CODE) references CATEGORY(CODE)
);

drop table IF EXISTS  MESSAGE;
create table MESSAGE
(
    ID BIGINT not null,
    TOKEN VARCHAR(255) not null,
    CONTENT VARCHAR(255),
    USERNAME VARCHAR(255),
    R_ANNOUNCE BIGINT,
    R_USER_ID BIGINT,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    CANCELLED BOOLEAN not null,
    primary key (ID, TOKEN),
    foreign key (R_ANNOUNCE) references ANNOUNCE(ID),
    foreign key (R_USER_ID) references TP_USER(ID)
);

drop table IF EXISTS  RESERVATION;
create table RESERVATION
(
    ID BIGINT   auto_increment
        primary key,
    DESCRIPTION VARCHAR(255),
    WEIGTH DECIMAL(19,2) not null,
    VALIDATE VARCHAR(10) default 'INSERTED',
    STATUS VARCHAR(10),
    R_ANNOUNCE_ID BIGINT,
    R_USER_ID BIGINT,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    CANCELLED BOOLEAN not null,

    foreign key (R_ANNOUNCE_ID) references ANNOUNCE(ID),
    foreign key (R_USER_ID) references TP_USER(ID)
);

drop table IF EXISTS  RESERVATION_CATEGORY;
create table RESERVATION_CATEGORY
(
    RESERVATION_ID BIGINT not null,
    CATEGORIES_CODE VARCHAR(15) not null,
    primary key (RESERVATION_ID, CATEGORIES_CODE),
    foreign key (RESERVATION_ID) references RESERVATION(ID),
    foreign key (CATEGORIES_CODE) references CATEGORY(CODE)
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
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    PRIMARY KEY (ID),
    foreign key (R_USER_ID) references TP_USER(ID)
);

drop table IF EXISTS  SUBSCRIBERS;
create table SUBSCRIBERS
(
    R_USER_ID BIGINT not null,
    SUBSCRIBER_ID BIGINT not null,
    primary key (R_USER_ID, SUBSCRIBER_ID),
    foreign key (R_USER_ID) references TP_USER(ID),
    foreign key (SUBSCRIBER_ID) references TP_USER(ID)
);

drop table IF EXISTS  SUBSCRIPTIONS;
create table SUBSCRIPTIONS
(
    SUBSCRIPTION_ID BIGINT not null,
    R_USER_ID BIGINT not null,
    primary key (SUBSCRIPTION_ID, R_USER_ID),
    foreign key (R_USER_ID) references TP_USER(ID),
    foreign key (SUBSCRIPTION_ID) references TP_USER(ID)
);

create unique index PRIMARY_KEY_273
    on TP_USER (ID);

drop table IF EXISTS  TP_USER_COMMUNICATION;
create table USER_COMMUNICATION
(
    USERS_ID BIGINT not null,
    COMMUNICATIONS_ID BIGINT not null,
    primary key (USERS_ID, COMMUNICATIONS_ID),
    foreign key (USERS_ID) references TP_USER(ID),
    foreign key (COMMUNICATIONS_ID) references COMMUNICATION(ID)
);

drop table IF EXISTS  USER_NOTIFICATION;
create table USER_NOTIFICATION
(
    USERS_ID BIGINT not null,
    NOTIFICATIONS_ID BIGINT not null,
    primary key (USERS_ID, NOTIFICATIONS_ID),
    foreign key (NOTIFICATIONS_ID) references NOTIFICATION(ID),
    foreign key (USERS_ID) references TP_USER(ID)
);

drop table IF EXISTS  USER_ROLE;
create table USER_ROLE
(
    R_USER BIGINT not null,
    ROLE_ID INTEGER not null,
    primary key (R_USER, ROLE_ID),
    foreign key (R_USER) references TP_USER(ID),
    foreign key (ROLE_ID) references ROLE(ID)
);
