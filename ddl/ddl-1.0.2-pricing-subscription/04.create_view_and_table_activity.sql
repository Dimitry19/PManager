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