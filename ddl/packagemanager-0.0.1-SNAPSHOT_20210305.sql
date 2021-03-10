--Sequeunces batch
--create sequence HIBERNATE_SEQUENCE;
--create sequence BATCH_STEP_EXECUTION_SEQ;
--create sequence BATCH_JOB_EXECUTION_SEQ;
--create sequence BATCH_JOB_SEQ;


--Tables
create table AIRLINE
(
    CODE        VARCHAR(255) not null,
    TOKEN       VARCHAR(255) not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    CANCELLED   BOOLEAN      not null,
    DESCRIPTION VARCHAR(255)
);

create table PROD_CATEGORY
(
    CODE        VARCHAR(255) not null,
    DESCRIPTION VARCHAR(255)
);

create table ROLE
(
    ID          INTEGER auto_increment,
    DESCRIPTION VARCHAR(255) not null
);

create table USER
(
    ID            BIGINT auto_increment,
    DATECREATED   TIMESTAMP,
    LASTUPDATED   TIMESTAMP,
    ACTIVE        INTEGER      not null,
    CANCELLED     BOOLEAN      not null,
    CONFIRM_TOKEN VARCHAR(255),
    EMAIL         VARCHAR(255) not null
        constraint UK_EJFK3G58OXSGBB4JU3U4FHIVK
            unique,
    FACEBOOK_ID   VARCHAR(255),
    FIRST_NAME    VARCHAR(255) not null,
    GENDER        VARCHAR(10),
    GOOGLE_ID     VARCHAR(255),
    LAST_NAME     VARCHAR(255) not null,
    PASSWORD      VARCHAR(255) not null,
    PHONE         VARCHAR(35)  not null,
    USERNAME      VARCHAR(15)  not null
        constraint UK_LB5YRVW2C22IM784WWRPWUQ06
            unique,
    constraint UK_31RMPFLL2ABII4QBEFW4B6O2P
        unique (EMAIL, USERNAME)
);

create table ANNOUNCE
(
    ID            BIGINT auto_increment,
    DATECREATED   TIMESTAMP,
    LASTUPDATED   TIMESTAMP,
    TOKEN         VARCHAR(255)   not null,
    ANNOUNCE_TYPE VARCHAR(10),
    ARRIVAL       VARCHAR(255)   not null,
    CANCELLED     BOOLEAN        not null,
    DEPARTURE     VARCHAR(255)   not null,
    DESCRIPTION   VARCHAR(255)   not null,
    END_DATE      TIMESTAMP      not null,
    GOLD_PRICE    DECIMAL(19, 2) not null,
    PRENIUM_PRICE DECIMAL(19, 2) not null,
    PRICE         DECIMAL(19, 2) not null,
    START_DATE    TIMESTAMP      not null,
    STATUS        VARCHAR(10),
    TRANSPORT     VARCHAR(255)   not null,
    WEIGHT        DECIMAL(19, 2) not null,
    R_CATEGORY    VARCHAR(255),
    R_USER_ID     BIGINT,
    constraint FK5FOX7Q27VFLBX6228XIKFRGSI
        foreign key (R_CATEGORY) references PROD_CATEGORY (CODE),
    constraint FKFW0TKPEM5N1S5HA75UD6QJMQ
        foreign key (R_USER_ID) references USER (ID)
);

create table MESSAGE
(
    ID          BIGINT       not null,
    TOKEN       VARCHAR(255) not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    CANCELLED   BOOLEAN      not null,
    CONTENT     VARCHAR(255),
    R_ANNOUNCE  BIGINT,
    R_USER_ID   BIGINT,
    constraint FKE39G9PLVG5K7X0GDURX5IIDLK
        foreign key (R_ANNOUNCE) references ANNOUNCE (ID),
    constraint FKOGVI3LF5JH16WQIAXRRPJSXMA
        foreign key (R_USER_ID) references USER (ID)
);

create table REVIEW
(
    ID          BIGINT auto_increment,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    TOKEN       VARCHAR(5)    not null,
    DETAILS     VARCHAR(5000) not null,
    TITLE       VARCHAR(35)   not null,
    RATING      INTEGER       not null,
    CANCELLED   BOOLEAN       not null,
    INDEXES     INTEGER       not null,
    R_USER_ID   BIGINT        not null,
    RATING_USER_ID   BIGINT   not null,
    constraint FK5PVU4WR9TFJ9B915TDEPMJAQ1
        foreign key (R_USER_ID) references USER (ID)
);

create table USER_ROLE
(
    R_USER  BIGINT  not null,
    ROLE_ID INTEGER not null,
    constraint FKH3WNT7IJXYT7F1JS45H8W3FOH
        foreign key (R_USER) references USER (ID),
    constraint FKN1RN9QODD3U4LE8UF3KL33QE3
        foreign key (ROLE_ID) references ROLE (ID)
);

/*
create table BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT auto_increment,
    VERSION         BIGINT,
    JOB_NAME        VARCHAR(100) not null,
    JOB_KEY         VARCHAR(32)  not null,
    constraint JOB_INST_UN
        unique (JOB_NAME, JOB_KEY)
);
create table BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID           BIGINT auto_increment,
    VERSION                    BIGINT,
    JOB_INSTANCE_ID            BIGINT    not null,
    CREATE_TIME                TIMESTAMP not null,
    START_TIME                 TIMESTAMP default NULL,
    END_TIME                   TIMESTAMP default NULL,
    STATUS                     VARCHAR(10),
    EXIT_CODE                  VARCHAR(2500),
    EXIT_MESSAGE               VARCHAR(2500),
    LAST_UPDATED               TIMESTAMP,
    JOB_CONFIGURATION_LOCATION VARCHAR(2500),
    constraint JOB_INST_EXEC_FK
        foreign key (JOB_INSTANCE_ID) references BATCH_JOB_INSTANCE (JOB_INSTANCE_ID)
);

create table BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT       not null,
    TYPE_CD          VARCHAR(6)   not null,
    KEY_NAME         VARCHAR(100) not null,
    STRING_VAL       VARCHAR(250),
    DATE_VAL         TIMESTAMP default NULL,
    LONG_VAL         BIGINT,
    DOUBLE_VAL       DOUBLE PRECISION,
    IDENTIFYING      CHAR(1)      not null,
    constraint JOB_EXEC_PARAMS_FK
        foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

create table BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  BIGINT auto_increment,
    VERSION            BIGINT       not null,
    STEP_NAME          VARCHAR(100) not null,
    JOB_EXECUTION_ID   BIGINT       not null,
    START_TIME         TIMESTAMP    not null,
    END_TIME           TIMESTAMP default NULL,
    STATUS             VARCHAR(10),
    COMMIT_COUNT       BIGINT,
    READ_COUNT         BIGINT,
    FILTER_COUNT       BIGINT,
    WRITE_COUNT        BIGINT,
    READ_SKIP_COUNT    BIGINT,
    WRITE_SKIP_COUNT   BIGINT,
    PROCESS_SKIP_COUNT BIGINT,
    ROLLBACK_COUNT     BIGINT,
    EXIT_CODE          VARCHAR(2500),
    EXIT_MESSAGE       VARCHAR(2500),
    LAST_UPDATED       TIMESTAMP,
    constraint JOB_EXEC_STEP_FK
        foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);
*/
/*
create table BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  BIGINT        not null,
    SHORT_CONTEXT      VARCHAR(2500) not null,
    SERIALIZED_CONTEXT LONGVARCHAR,
    constraint STEP_EXEC_CTX_FK
        foreign key (STEP_EXECUTION_ID) references BATCH_STEP_EXECUTION (STEP_EXECUTION_ID)
);

create table BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   BIGINT        not null,
    SHORT_CONTEXT      VARCHAR(2500) not null,
    SERIALIZED_CONTEXT LONGVARCHAR,
    constraint JOB_EXEC_CTX_FK
        foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

*/