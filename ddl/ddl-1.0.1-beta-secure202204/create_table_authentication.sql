create table tp_authentication
(
    ID bigint auto_increment
        primary key,
    EMAIL varchar(255) not null,
    NAME varchar(255) not null,
    USERNAME varchar(255) not null,
    DATECREATED timestamp null,
    LASTUPDATED timestamp null,
    CANCELLED tinyint(1) not null
);





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
