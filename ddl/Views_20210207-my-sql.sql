DROP VIEW  VALID_RESERVATION_CREATED;
DROP VIEW  VALID_RESERVATION_RECEIVED;
DROP VIEW  V_RESERVATION_CATEGORY;
DROP VIEW  V_RESERVATION_CREATED;
DROP VIEW  V_RESERVATION_RECEIVED;



CREATE VIEW  VALID_RESERVATION_CREATED(ID, DESCRIPTION, WEIGTH, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
                                             START_DATE, END_DATE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME,
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
       U.ID AS R_USER_ID,
       U.FIRST_NAME,
       U.LAST_NAME,
       U.USERNAME,
       U.EMAIL,
       U.PHONE,
       U.GENDER
FROM RESERVATION R
         INNER JOIN ANNOUNCE A
                    ON R.R_ANNOUNCE_ID = A.ID
         INNER JOIN TP_USER U
                    ON R.R_USER_ID = U.ID
WHERE ((R.R_USER_ID = U.ID)
    AND (A.ID = R.R_ANNOUNCE_ID))
  AND ((R.STATUS = 'VALID')
    AND ((A.STATUS = 'VALID')
        AND ((R.CANCELLED IS FALSE)
            AND (A.CANCELLED IS FALSE))));

CREATE  VIEW VALID_RESERVATION_RECEIVED(ID, DESCRIPTION, WEIGTH, VALIDATE,DATECREATED, R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
                                              START_DATE, END_DATE, R_USER_ID, FIRST_NAME, LAST_NAME, USERNAME,
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
       U.ID AS R_USER_ID,
       U.FIRST_NAME,
       U.LAST_NAME,
       U.USERNAME,
       U.EMAIL,
       U.PHONE,
       U.GENDER
FROM RESERVATION R
         INNER JOIN ANNOUNCE A
                    ON R.R_ANNOUNCE_ID = A.ID
         INNER JOIN TP_USER U
                    ON R.R_USER_ID = U.ID
WHERE ((A.R_USER_ID = U.ID)
    AND (A.ID = R.R_ANNOUNCE_ID))
  AND ((R.STATUS = 'VALID')
    AND ((A.STATUS = 'VALID')
        AND ((R.CANCELLED IS FALSE)
            AND (A.CANCELLED IS FALSE))));

CREATE  VIEW V_RESERVATION_CATEGORY(ID, R_ID, CODE) AS
SELECT CONCAT_WS('_', RESERVATION_ID, CATEGORIES_CODE) AS ID,
       RESERVATION_ID                                  AS R_ID,
       CATEGORIES_CODE                                 AS CODE
FROM RESERVATION_CATEGORY;

CREATE VIEW V_RESERVATION_CREATED(ID, DESCRIPTION, WEIGTH, VALIDATE, DATECREATED,R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
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
FROM RESERVATION R
         INNER JOIN ANNOUNCE A
                    ON R.R_ANNOUNCE_ID = A.ID
         INNER JOIN TP_USER U
                    ON R.R_USER_ID = U.ID
WHERE (R.R_USER_ID = U.ID)
  AND (A.ID = R.R_ANNOUNCE_ID);

CREATE  VIEW V_RESERVATION_RECEIVED(ID, DESCRIPTION, WEIGTH, VALIDATE, DATECREATED,R_ANNOUNCE_ID, DEPARTURE, ARRIVAL,
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
FROM RESERVATION R
         INNER JOIN ANNOUNCE A
                    ON R.R_ANNOUNCE_ID = A.ID
         INNER JOIN TP_USER U
                    ON R.R_USER_ID = U.ID
WHERE (A.R_USER_ID = U.ID)
  AND (A.ID = R.R_ANNOUNCE_ID);




