create schema PUBLIC;

create table AIRLINE
(
	CODE VARCHAR(255) not null,
	TOKEN VARCHAR(255) not null,
	DATECREATED TIMESTAMP(26,6),
	LASTUPDATED TIMESTAMP(26,6),
	CANCELLED BOOLEAN not null,
	DESCRIPTION VARCHAR(255),
	primary key (CODE, TOKEN)
)
;

create table PROD_CATEGORY
(
	CODE VARCHAR(255) not null,
	TOKEN VARCHAR(255) not null,
	DATECREATED TIMESTAMP(26,6),
	LASTUPDATED TIMESTAMP(26,6),
	CANCELLED BOOLEAN not null,
	DESCRIPTION VARCHAR(255),
	primary key (CODE, TOKEN)
)
;

create table ROLE
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_1A9EBA29_1748_4F49_9334_5C117FB3AFB4)
		primary key,
	DESCRIPTION VARCHAR(255) not null
)
;

create table USER
(
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_73C99A1A_2D42_4E9B_A174_01B34DCB8F8A)
		primary key,
	DATECREATED TIMESTAMP(26,6),
	LASTUPDATED TIMESTAMP(26,6),
	ACTIVE INTEGER not null,
	CANCELLED BOOLEAN not null,
	CONFIRM_TOKEN VARCHAR(255),
	EMAIL VARCHAR(255) not null
		unique,
	FACEBOOK_ID VARCHAR(255),
	FIRST_NAME VARCHAR(255) not null,
	GENDER VARCHAR(10),
	GOOGLE_ID VARCHAR(255),
	LAST_NAME VARCHAR(255) not null,
	PASSWORD VARCHAR(255) not null,
	PHONE VARCHAR(35) not null,
	USERNAME VARCHAR(15) not null
		unique
)
;

create table ANNOUNCE
(
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_BBB28528_980C_4878_BF53_6F1A60F0BC23)
		primary key,
	DATECREATED TIMESTAMP(26,6),
	LASTUPDATED TIMESTAMP(26,6),
	TOKEN VARCHAR(255) not null,
	DESCRIPTION VARCHAR(255),
	ANNOUNCETYPE VARCHAR(10),
	ARRIVAL VARCHAR(255) not null,
	CANCELLED BOOLEAN not null,
	DEPARTURE VARCHAR(255) not null,
	END_DATE DATE(10) not null,
	PRENIUM_PRICE DECIMAL(19,2) not null,
	GOLD_PRICE DECIMAL(19,2) not null,
	PRICE DECIMAL(19,2) not null,
	START_DATE DATE(10) not null,
	STATUS VARCHAR(10),
	WEIGHT DECIMAL(19,2) not null,
	AIRLINE_CODE VARCHAR(255),
	AIRLINE_TOKEN VARCHAR(255),
	R_USER_ID BIGINT,
	constraint FK4PT5RDXBCBWH4BQTPKW4BP6ER
		foreign key (AIRLINE_TOKEN) references AIRLINE (TOKEN),
	constraint FKFW0TKPEM5N1S5HA75UD6QJMQ
		foreign key (R_USER_ID) references USER
)
;

create index FK4PT5RDXBCBWH4BQTPKW4BP6ER_INDEX_7
	on ANNOUNCE (AIRLINE_CODE, AIRLINE_TOKEN)
;

create table MESSAGE
(
	ID BIGINT not null,
	TOKEN VARCHAR(255) not null,
	DATECREATED TIMESTAMP(26,6),
	LASTUPDATED TIMESTAMP(26,6),
	CANCELLED BOOLEAN not null,
	CONTENT VARCHAR(255),
	R_ANNOUNCE BIGINT,
	R_USER_ID BIGINT,
	primary key (ID, TOKEN),
	constraint FKE39G9PLVG5K7X0GDURX5IIDLK
		foreign key (R_ANNOUNCE) references ANNOUNCE,
	constraint FKOGVI3LF5JH16WQIAXRRPJSXMA
		foreign key (R_USER_ID) references USER
)
;

create table USER_ROLE
(
	R_USER BIGINT not null,
	ROLE_ID INTEGER not null,
	primary key (R_USER, ROLE_ID),
	constraint FKH3WNT7IJXYT7F1JS45H8W3FOH
		foreign key (R_USER) references USER,
	constraint FKN1RN9QODD3U4LE8UF3KL33QE3
		foreign key (ROLE_ID) references ROLE
)
;

