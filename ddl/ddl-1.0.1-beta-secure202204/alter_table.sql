-- Ces scripts sont utiles seulement quand les tables ont deja ete crees
-- Scripts à executer sur MySql
ALTER TABLE tp_user
    ADD COLUMN MULTIPLE_FACTOR_AUTH   tinyint(1) not null AFTER PASSWORD;

ALTER TABLE tp_user
    ADD COLUMN MFA_SECRET  varchar(255) not null  AFTER MULTIPLE_FACTOR_AUTH;

ALTER TABLE announce
    ADD COLUMN CODE  varchar(10)    AFTER ID;

update announce B,
(SELECT ID, CASE WHEN (ID < 10) THEN  CONCAT(CONCAT(concat('TP',LEFT(ANNOUNCE_TYPE,1)), CONCAT_WS('','0000',ID) ),SUBSTRING(YEAR(DATECREATED),3))
                 ELSE CONCAT(CONCAT(concat('TP',LEFT(ANNOUNCE_TYPE,1)), CONCAT_WS('','000',ID) ),SUBSTRING(YEAR(DATECREATED),3))
    END as  CODE
 from announce
) A
set B.CODE = A.CODE
    WHERE B.ID = A.ID;

ALTER TABLE tp_user
    ADD COLUMN   PAYS varchar(35) not null;

ALTER TABLE tp_user
    ADD COLUMN  VILLE varchar(50) not null;



update  message set TOKEN ='TP';
update  review set TOKEN ='TP';
update  airline set TOKEN ='TP';





--Scripts à executer sur H2----

ALTER TABLE tp_user
    ADD COLUMN MULTIPLE_FACTOR_AUTH   BOOLEAN not null default true;

ALTER TABLE tp_user
    ADD COLUMN MFA_SECRET  varchar(255) ;



ALTER TABLE announce
    ADD COLUMN CODE  varchar(10) ;

update announce B set CODE= (SELECT CASE WHEN (ID < 10) THEN  CONCAT(CONCAT(concat('TP',LEFT(ANNOUNCE_TYPE,1)), CONCAT_WS('','0000',ID) ),SUBSTRING(YEAR(DATECREATED),3))
                                         ELSE CONCAT(CONCAT(concat('TP',LEFT(ANNOUNCE_TYPE,1)), CONCAT_WS('','000',ID) ),SUBSTRING(YEAR(DATECREATED),3))
                                        END as  CODE
                             from announce A
                             where A.ID = B.ID)
WHERE EXISTS(
              SELECT 1 FROM announce
          );

update  message set TOKEN ='TP';
update  review set TOKEN ='TP';
update  airline set TOKEN ='TP';


ALTER TABLE announce
    ADD COLUMN ESTIMATE_VALUE decimal(19,2);