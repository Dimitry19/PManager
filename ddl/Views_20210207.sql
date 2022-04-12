create schema if not exists VIEWS;

DROP VIEW  IF EXISTS VIEWS.VALID_RESERVATION_CREATED;


CREATE  VIEW VIEWS.VALID_RESERVATION_CREATED(ID, DESCRIPTION, WEIGTH, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
              START_DATE, END_DATE, R_USER_ANNOUNCE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME,
              EMAIL, PHONE, GENDER) AS
       SELECT R.ID,
              R.DESCRIPTION,
              R.WEIGTH,
              R.VALIDATE,
              R.DATECREATED,
              A.ID AS R_ANNOUNCE_ID,
              A.DEPARTURE,
              A.ARRIVAL,
              A.START_DATE,
              A.END_DATE,
              A.R_USER_ID AS R_USER_ANNOUNCE,
              U.ID AS R_USER_ID,
              U.FIRST_NAME,
              U.LAST_NAME,
              U.USERNAME,
              U.EMAIL,
              U.PHONE,
              U.GENDER
       FROM PUBLIC.RESERVATION R
              INNER JOIN PUBLIC.TP_USER U
                     ON R.R_USER_ID = U.ID
              INNER JOIN PUBLIC.ANNOUNCE A
                     ON R.R_ANNOUNCE_ID = A.ID
       WHERE A.STATUS = 'VALID' AND R.STATUS ='VALID'
             AND R.CANCELLED IS FALSE  AND A.CANCELLED IS FALSE
             AND U.CANCELLED IS FALSE;




DROP VIEW IF EXISTS VIEWS.VALID_RESERVATION_RECEIVED;

CREATE  VIEW VIEWS.VALID_RESERVATION_RECEIVED(ID, DESCRIPTION, WEIGTH, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
              START_DATE, END_DATE, R_USER_RESERVATION,R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME,
              EMAIL, PHONE, GENDER) AS
       SELECT R.ID,
              R.DESCRIPTION,
              R.WEIGTH,
              R.VALIDATE,
              R.DATECREATED,
              A.ID AS R_ANNOUNCE_ID,
              A.DEPARTURE,
              A.ARRIVAL,
              A.START_DATE,
              A.END_DATE,
              R.R_USER_ID AS R_USER_RESERVATION,
              U.ID AS R_USER_ID,
              U.FIRST_NAME,
              U.LAST_NAME,
              U.USERNAME,
              U.EMAIL,
              U.PHONE,
              U.GENDER
       FROM PUBLIC.RESERVATION R
              INNER JOIN PUBLIC.ANNOUNCE A
                     ON R.R_ANNOUNCE_ID = A.ID
              INNER JOIN PUBLIC.TP_USER U
                     ON A.R_USER_ID = U.ID
       WHERE A.STATUS = 'VALID' AND R.STATUS ='VALID'
             AND R.CANCELLED IS FALSE  AND A.CANCELLED IS FALSE
             AND U.CANCELLED IS FALSE;


DROP VIEW IF EXISTS VIEWS.V_RESERVATION_CATEGORY;


CREATE  VIEW VIEWS.V_RESERVATION_CATEGORY(ID, R_ID, CODE) AS
       SELECT CONCAT_WS('_', RESERVATION_ID, CATEGORIES_CODE) AS ID,
              RESERVATION_ID                                  AS R_ID,
              CATEGORIES_CODE                                 AS CODE
       FROM PUBLIC.RESERVATION_CATEGORY;


DROP VIEW  IF EXISTS VIEWS.V_RESERVATION_CREATED;

CREATE  VIEW VIEWS.V_RESERVATION_CREATED(ID, DESCRIPTION, WEIGTH, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
              START_DATE, END_DATE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME, EMAIL,
              PHONE, GENDER) AS
       SELECT R.ID,
              R.DESCRIPTION,
              R.WEIGTH,
              R.VALIDATE,
              R.DATECREATED,
              A.ID AS R_ANNOUNCE_ID,
              A.DEPARTURE,
              A.ARRIVAL,
              A.START_DATE,
              A.END_DATE,
              U.ID AS R_USER_ID,
              U.FIRST_NAME,
              U.LAST_NAME,
              U.USERNAME,
              U.EMAIL,
              U.PHONE,
              U.GENDER
       FROM PUBLIC.RESERVATION R
              INNER JOIN PUBLIC.ANNOUNCE A
                     ON R.R_ANNOUNCE_ID = A.ID
              INNER JOIN PUBLIC.TP_USER U
                     ON R.R_USER_ID = U.ID
       WHERE (R.R_USER_ID = U.ID)
             AND (A.ID = R.R_ANNOUNCE_ID);



DROP VIEW IF EXISTS VIEWS.V_RESERVATION_RECEIVED;

CREATE VIEW VIEWS.V_RESERVATION_RECEIVED(ID, DESCRIPTION, WEIGTH, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
              START_DATE, END_DATE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME, EMAIL,
              PHONE, GENDER) AS
       SELECT R.ID,
              R.DESCRIPTION,
              R.WEIGTH,
              R.VALIDATE,
              R.DATECREATED,
              A.ID AS R_ANNOUNCE_ID,
              A.DEPARTURE,
              A.ARRIVAL,
              A.START_DATE,
              A.END_DATE,
              U.ID AS R_USER_ID,
              U.FIRST_NAME,
              U.LAST_NAME,
              U.USERNAME,
              U.EMAIL,
              U.PHONE,
              U.GENDER
       FROM PUBLIC.RESERVATION R
              INNER JOIN PUBLIC.ANNOUNCE A
                     ON R.R_ANNOUNCE_ID = A.ID
              INNER JOIN PUBLIC.TP_USER U
                     ON R.R_USER_ID = U.ID
       WHERE (A.R_USER_ID = U.ID)
             AND (A.ID = R.R_ANNOUNCE_ID);

