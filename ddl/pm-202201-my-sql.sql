create schema tp_manager collate utf8mb4_0900_ai_ci;

create table administrator
(
       ID bigint auto_increment
              primary key,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       EMAIL varchar(255) not null,
       NAME varchar(255) not null,
       USERNAME varchar(255) not null,
       CANCELLED tinyint(1) not null
);

create table airline
(
       CODE varchar(255) not null,
       TOKEN varchar(255) not null,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CANCELLED tinyint(1) not null,
       DESCRIPTION varchar(255) null,
       primary key (CODE, TOKEN)
);

create table batch_job_instance
(
       JOB_INSTANCE_ID bigint auto_increment
              primary key,
       VERSION bigint null,
       JOB_NAME varchar(100) not null,
       JOB_KEY varchar(32) not null,
       constraint JOB_NAME
              unique (JOB_NAME, JOB_KEY)
);

create table batch_job_execution
(
       JOB_EXECUTION_ID bigint auto_increment
              primary key,
       VERSION bigint null,
       JOB_INSTANCE_ID bigint not null,
       CREATE_TIME timestamp not null,
       START_TIME timestamp null,
       END_TIME timestamp null,
       STATUS varchar(10) null,
       EXIT_CODE varchar(2500) null,
       EXIT_MESSAGE varchar(2500) null,
       LAST_UPDATED timestamp null,
       JOB_CONFIGURATION_LOCATION varchar(2500) null,
       constraint batch_job_execution_ibfk_1
              foreign key (JOB_INSTANCE_ID) references batch_job_instance (JOB_INSTANCE_ID)
);

create index JOB_INSTANCE_ID
       on batch_job_execution (JOB_INSTANCE_ID);

create table batch_job_execution_context
(
       JOB_EXECUTION_ID bigint not null
              primary key,
       SHORT_CONTEXT varchar(2500) not null,
       SERIALIZED_CONTEXT varchar(2500) null,
       constraint batch_job_execution_context_ibfk_1
              foreign key (JOB_EXECUTION_ID) references batch_job_execution (JOB_EXECUTION_ID)
);

create table batch_job_execution_params
(
       JOB_EXECUTION_ID bigint not null,
       TYPE_CD varchar(6) not null,
       KEY_NAME varchar(100) not null,
       STRING_VAL varchar(250) null,
       DATE_VAL timestamp null,
       LONG_VAL bigint null,
       DOUBLE_VAL double null,
       IDENTIFYING char not null,
       constraint batch_job_execution_params_ibfk_1
              foreign key (JOB_EXECUTION_ID) references batch_job_execution (JOB_EXECUTION_ID)
);

create index JOB_EXECUTION_ID
       on batch_job_execution_params (JOB_EXECUTION_ID);

create table batch_step_execution
(
       STEP_EXECUTION_ID bigint auto_increment
              primary key,
       VERSION bigint not null,
       STEP_NAME varchar(100) not null,
       JOB_EXECUTION_ID bigint not null,
       START_TIME timestamp not null,
       END_TIME timestamp null,
       STATUS varchar(10) null,
       COMMIT_COUNT bigint null,
       READ_COUNT bigint null,
       FILTER_COUNT bigint null,
       WRITE_COUNT bigint null,
       READ_SKIP_COUNT bigint null,
       WRITE_SKIP_COUNT bigint null,
       PROCESS_SKIP_COUNT bigint null,
       ROLLBACK_COUNT bigint null,
       EXIT_CODE varchar(2500) null,
       EXIT_MESSAGE varchar(2500) null,
       LAST_UPDATED timestamp null,
       constraint batch_step_execution_ibfk_1
              foreign key (JOB_EXECUTION_ID) references batch_job_execution (JOB_EXECUTION_ID)
);

create index JOB_EXECUTION_ID
       on batch_step_execution (JOB_EXECUTION_ID);

create table batch_step_execution_context
(
       STEP_EXECUTION_ID bigint not null
              primary key,
       SHORT_CONTEXT varchar(2500) not null,
       SERIALIZED_CONTEXT varchar(2500) null,
       constraint batch_step_execution_context_ibfk_1
              foreign key (STEP_EXECUTION_ID) references batch_step_execution (STEP_EXECUTION_ID)
);

create table category
(
       CODE varchar(15) not null
              primary key,
       DESCRIPTION varchar(255) not null
);

create table city
(
       ID varchar(50) not null
              primary key,
       NAME varchar(255) not null
);

create table communication
(
       ID bigint auto_increment
              primary key,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CONTENT varchar(255) not null,
       TYPE varchar(255) not null,
       R_ADMIN_ID bigint null,
       CANCELLED tinyint(1) not null
);

create table contact_us
(
       ID bigint auto_increment
              primary key,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CONTENT varchar(500) not null,
       RECEIVER varchar(255) not null,
       SENDER varchar(255) not null,
       SUBJECT varchar(255) not null,
       PSEUDO_SENDER varchar(255) null
);

create table image
(
       ID bigint auto_increment
              primary key,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       NAME varchar(255) not null,
       TYPE varchar(255) null,
       ORIGIN varchar(255) null,
       PIC_BYTE longblob null,
       constraint NAME
              unique (NAME)
);

create table notification
(
       ID bigint auto_increment
              primary key,
       CANCELLED tinyint(1) not null,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       R_ANNOUNCE_ID bigint null,
       MESSAGE varchar(255) not null,
       R_USER_ID bigint null,
       SESSION_ID varchar(255) null,
       STATUS varchar(10) not null,
       TITLE varchar(60) not null,
       TYPE varchar(10) not null,
       USER_ID bigint not null
);

create table role
(
       ID int auto_increment
              primary key,
       DESCRIPTION varchar(255) not null
);

create table tp_authentication
(
    ID BIGINT auto_increment,
    ATTEMPT INTEGER not null,
    DESACTIVATE BOOLEAN not null,
    R_USER_ID BIGINT,
    CANCELLED BOOLEAN not null,
    DATECREATED TIMESTAMP,
    LASTUPDATED TIMESTAMP,
    foreign key (R_USER_ID) references TP_USER (ID)
);

create table tp_user
(
       ID bigint auto_increment,
       FIRST_NAME varchar(255) not null,
       LAST_NAME varchar(255) not null,
       GENDER varchar(10) null,
       PHONE varchar(35) not null,
       EMAIL varchar(255) not null,
       USERNAME varchar(15) not null,
       PASSWORD varchar(255) not null,
       MULTIPLE_FACTOR_AUTH   tinyint(1) not null,
       MFA_SECRET  varchar(255) not null,
       ENABLE_NOTIF tinyint(1) not null,
       ACTIVE int not null,
       CONFIRM_TOKEN varchar(255) null,
       FACEBOOK_ID varchar(255) null,
       GOOGLE_ID varchar(255) null,
       IMAGE_ID bigint null,
       ERROR varchar(255) null,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CANCELLED tinyint(1) not null,
       constraint EMAIL
              unique (EMAIL),
       constraint EMAIL_2
              unique (EMAIL, USERNAME),
       constraint PRIMARY_KEY_273
              unique (ID),
       constraint USERNAME
              unique (USERNAME),
       constraint tp_user_ibfk_1
              foreign key (IMAGE_ID) references image (ID)
);

create index IMAGE_ID
       on tp_user (IMAGE_ID);

alter table tp_user
       add primary key (ID);

create table announce
(
       ID bigint auto_increment,
       CODE varchar(10) not null,
       TOKEN varchar(255) not null,
       ANNOUNCE_TYPE varchar(10) null,
       DEPARTURE varchar(255) not null,
       ARRIVAL varchar(255) not null,
       DESCRIPTION varchar(500) not null,
       TRANSPORT varchar(255) not null,
       WEIGHT decimal(19,2) not null,
       REMAIN_WEIGHT decimal(19,2) null,
       START_DATE timestamp not null,
       END_DATE timestamp not null,
       PRICE decimal(19,2) not null,
       GOLD_PRICE decimal(19,2) not null,
       PRENIUM_PRICE decimal(19,2) not null,
       R_USER_ID bigint null,
       IMAGE_ID bigint null,
       STATUS varchar(10) null,
       --COUNTRESERVATION int null,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CANCELLED tinyint(1) not null,
       constraint PRIMARY_KEY_B9
              unique (ID),
       constraint announce_ibfk_1
              foreign key (R_USER_ID) references tp_user (ID),
       constraint announce_ibfk_2
              foreign key (IMAGE_ID) references image (ID)
);

create index IMAGE_ID
       on announce (IMAGE_ID);

create index R_USER_ID
       on announce (R_USER_ID);

alter table announce
       add primary key (ID);

create table announce_category
(
       ANNOUNCE_ID bigint not null,
       CATEGORIES_CODE varchar(15) not null,
       primary key (ANNOUNCE_ID, CATEGORIES_CODE),
       constraint announce_category_ibfk_1
              foreign key (ANNOUNCE_ID) references announce (ID),
       constraint announce_category_ibfk_2
              foreign key (CATEGORIES_CODE) references category (CODE)
);

create index CATEGORIES_CODE
       on announce_category (CATEGORIES_CODE);

create table message
(
       ID bigint not null,
       TOKEN varchar(255) not null,
       CONTENT varchar(255) null,
       R_ANNOUNCE bigint null,
       R_USER_ID bigint null,
       USERNAME varchar(255) null,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CANCELLED tinyint(1) not null,
       primary key (ID, TOKEN),
       constraint message_ibfk_1
              foreign key (R_ANNOUNCE) references announce (ID),
       constraint message_ibfk_2
              foreign key (R_USER_ID) references tp_user (ID)
);

create index R_ANNOUNCE
       on message (R_ANNOUNCE);

create index R_USER_ID
       on message (R_USER_ID);

create table reservation
(
       ID bigint auto_increment
              primary key,

       WEIGTH decimal(19,2) not null,
       DESCRIPTION varchar(255) null,
       R_ANNOUNCE_ID bigint null,
       R_USER_ID bigint null,
       VALIDATE varchar(10) default 'INSERTED' null,
       STATUS varchar(10) null,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CANCELLED tinyint(1) not null,
       constraint reservation_ibfk_1
              foreign key (R_ANNOUNCE_ID) references announce (ID),
       constraint reservation_ibfk_2
              foreign key (R_USER_ID) references tp_user (ID)
);

create index R_ANNOUNCE_ID
       on reservation (R_ANNOUNCE_ID);

create index R_USER_ID
       on reservation (R_USER_ID);

create table reservation_category
(
       RESERVATION_ID bigint not null,
       CATEGORIES_CODE varchar(15) not null,
       primary key (RESERVATION_ID, CATEGORIES_CODE),
       constraint reservation_category_ibfk_1
              foreign key (RESERVATION_ID) references reservation (ID),
       constraint reservation_category_ibfk_2
              foreign key (CATEGORIES_CODE) references category (CODE)
);

create index CATEGORIES_CODE
       on reservation_category (CATEGORIES_CODE);

create table review
(
       ID bigint auto_increment
              primary key,
       TOKEN varchar(5) not null,
       TITLE varchar(35) not null,
       DETAILS varchar(5000) not null,
       RATING int not null,
       INDEXES int not null,
       R_USER_ID bigint not null,
       RATING_USER_ID bigint null,
       DATECREATED timestamp null,
       LASTUPDATED timestamp null,
       CANCELLED tinyint(1) not null,
       constraint review_ibfk_1
              foreign key (R_USER_ID) references tp_user (ID)
);

create index R_USER_ID
       on review (R_USER_ID);

create table subscribers
(
       R_USER_ID bigint not null,
       SUBSCRIBER_ID bigint not null,
       primary key (R_USER_ID, SUBSCRIBER_ID),
       constraint subscribers_ibfk_1
              foreign key (R_USER_ID) references tp_user (ID),
       constraint subscribers_ibfk_2
              foreign key (SUBSCRIBER_ID) references tp_user (ID)
);

create index SUBSCRIBER_ID
       on subscribers (SUBSCRIBER_ID);

create table subscriptions
(
       SUBSCRIPTION_ID bigint not null,
       R_USER_ID bigint not null,
       primary key (SUBSCRIPTION_ID, R_USER_ID),
       constraint subscriptions_ibfk_1
              foreign key (R_USER_ID) references tp_user (ID),
       constraint subscriptions_ibfk_2
              foreign key (SUBSCRIPTION_ID) references tp_user (ID)
);

create index R_USER_ID
       on subscriptions (R_USER_ID);

create table tp_user_communication
(
       USERS_ID bigint not null,
       COMMUNICATIONS_ID bigint not null,
       primary key (USERS_ID, COMMUNICATIONS_ID),
       constraint tp_user_communication_ibfk_1
              foreign key (USERS_ID) references tp_user (ID),
       constraint tp_user_communication_ibfk_2
              foreign key (COMMUNICATIONS_ID) references communication (ID)
);

create index COMMUNICATIONS_ID
       on tp_user_communication (COMMUNICATIONS_ID);

create table user_communication
(
       USERS_ID bigint not null,
       COMMUNICATIONS_ID bigint not null,
       primary key (USERS_ID, COMMUNICATIONS_ID),
       constraint user_communication_ibfk_1
              foreign key (USERS_ID) references tp_user (ID),
       constraint user_communication_ibfk_2
              foreign key (COMMUNICATIONS_ID) references communication (ID)
);

create index COMMUNICATIONS_ID
       on user_communication (COMMUNICATIONS_ID);

create table user_notification
(
       USERS_ID bigint not null,
       NOTIFICATIONS_ID bigint not null,
       primary key (USERS_ID, NOTIFICATIONS_ID),
       constraint user_notification_ibfk_1
              foreign key (NOTIFICATIONS_ID) references notification (ID),
       constraint user_notification_ibfk_2
              foreign key (USERS_ID) references tp_user (ID)
);

create index NOTIFICATIONS_ID
       on user_notification (NOTIFICATIONS_ID);

create table user_role
(
       R_USER bigint not null,
       ROLE_ID int not null,
       primary key (R_USER, ROLE_ID),
       constraint user_role_ibfk_1
              foreign key (R_USER) references tp_user (ID),
       constraint user_role_ibfk_2
              foreign key (ROLE_ID) references role (ID)
);

create index ROLE_ID
       on user_role (ROLE_ID);

-- auto-generated definition
create table sms_otp(
        ID BIGINT auto_increment primary key,
        OTP_CODE     INTEGER      not null,
        unique(OTP_CODE),
        PHONE_NUMBER VARCHAR(255) not null,
        DATECREATED  TIMESTAMP,
        LASTUPDATED  TIMESTAMP,
        CANCELLED    BOOLEAN      not null
);