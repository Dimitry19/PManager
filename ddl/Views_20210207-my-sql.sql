
DROP VIEW  IF EXISTS valid_reservation_created;
DROP VIEW  IF EXISTS valid_reservation_received;
DROP VIEW  IF EXISTS v_reservation_category;
DROP VIEW  IF EXISTS v_reservation_created ;
DROP VIEW  IF EXISTS v_reservation_received;


CREATE  VIEW valid_reservation_created(ID, DESCRIPTION, WEIGTH,ESTIMATE_VALUE, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
                                       START_DATE, END_DATE, R_USER_ANNOUNCE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME,
                                       EMAIL, PHONE, GENDER) AS
SELECT R.ID,
       R.DESCRIPTION,
       R.WEIGTH,
       R.ESTIMATE_VALUE,
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
FROM reservation R
         INNER JOIN tp_user U
                    ON R.R_USER_ID = U.ID
         INNER JOIN announce A
                    ON R.R_ANNOUNCE_ID = A.ID
WHERE A.STATUS = 'VALID' AND R.STATUS ='VALID'
  AND R.CANCELLED IS FALSE  AND A.CANCELLED IS FALSE
  AND U.CANCELLED IS FALSE;




CREATE  VIEW valid_reservation_received(ID, DESCRIPTION, WEIGTH,ESTIMATE_VALUE, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
                                        START_DATE, END_DATE, R_USER_RESERVATION,R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME,
                                        EMAIL, PHONE,GENDER) AS
SELECT R.ID,
       R.DESCRIPTION,
       R.WEIGTH,
       R.ESTIMATE_VALUE,
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
FROM reservation R
         INNER JOIN announce A
                    ON R.R_ANNOUNCE_ID = A.ID
         INNER JOIN tp_user U
                    ON A.R_USER_ID = U.ID
WHERE A.STATUS = 'VALID' AND R.STATUS ='VALID'
  AND R.CANCELLED IS FALSE  AND A.CANCELLED IS FALSE
  AND U.CANCELLED IS FALSE;

CREATE  VIEW v_reservation_category(ID, R_ID, CODE) AS
SELECT CONCAT_WS('_', RESERVATION_ID, CATEGORIES_CODE) AS ID,
       RESERVATION_ID                                  AS R_ID,
       CATEGORIES_CODE                                 AS CODE
FROM reservation_category;

CREATE VIEW v_reservation_created(ID, DESCRIPTION, WEIGTH, ESTIMATE_VALUE,VALIDATE, DATECREATED,R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
                                  START_DATE, END_DATE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME, EMAIL,
                                  PHONE, GENDER) AS
SELECT R.ID,
       R.DESCRIPTION,
       R.WEIGTH,
       R.ESTIMATE_VALUE,
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
FROM reservation R
         INNER JOIN announce A
                    ON R.R_ANNOUNCE_ID = A.ID
         INNER JOIN tp_user U
                    ON R.R_USER_ID = U.ID
WHERE (R.R_USER_ID = U.ID)
  AND (A.ID = R.R_ANNOUNCE_ID);

CREATE  VIEW v_reservation_received(ID, DESCRIPTION, WEIGTH, ESTIMATE_VALUE,VALIDATE, DATECREATED,R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
                                    START_DATE, END_DATE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME, EMAIL,
                                    PHONE, GENDER) AS
SELECT R.ID,
       R.DESCRIPTION,
       R.WEIGTH,
       R.ESTIMATE_VALUE,
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
FROM reservation R
         INNER JOIN announce A
                    ON R.R_ANNOUNCE_ID = A.ID
         INNER JOIN tp_user U
                    ON R.R_USER_ID = U.ID
WHERE (A.R_USER_ID = U.ID)
  AND (A.ID = R.R_ANNOUNCE_ID);

