
/** 1.
 *   (a) Vytvorte tabuľku kontakty a pre dvoch 
 *   študentov vložte do tabuľky po jednom kontakte.
 */

 CREATE TABLE kontakty (
    ID NUMBER(38) NOT NULL PRIMARY KEY,
    ROD_CISLO CHAR(11) NOT NULL,
    POPIS VARCHAR2(10) NOT NULL,
    KONTAKT VARCHAR2(30) NOT NULL
);

select * from STUDENT;

insert into kontakty
values (1,'841106/3456', 'email', 'test@gmail.com');
insert into kontakty
values (3,'841106/3456', 'cislo', '0918000000');

insert into kontakty
values (2,'840312/7845', 'cislo', '0918569365');

select * from kontakty;

DESC kontakty;

/**
    (b) V sqlplus vypíšte zoznam študentov a ak majú nejaký 
    kontakt, tak ich vypíšte aj s kontaktom


 */



select meno, priezvisko, kontakt 
from OS_UDAJE left join kontakty using (ROD_CISLO);

/**
 *  (c) Vytvorte package Student_package a implementujte 
 *  metódu Vypis tak, aby vypísala študentom aj ich 
 *  kontakt, ak nejaký majú.
 *
 *  (d) Aspoň jednému študentovi vložte viac kontaktov a 
 *  upravte procedúru Vypis tak, aby vypísala študentom 
 *  všetky kontakty (každý do nového riadku, ale meno, 
 *  priezvisko študenta bude len raz).
 *
 *  package:
 *  https://www.youtube.com/watch?v=vUBZOgu1rc0&ab_channel=ManishSharma
 *  https://www.youtube.com/watch?v=Lqz78kOCx9k&ab_channel=ManishSharma
 */

CREATE or replace package Student_package IS
PROCEDURE vypis(p_rod_cislo char);
end Student_package;
/

create or replace package body Student_package is

    PROCEDURE vypis(p_rod_cislo char) is

        CURSOR c_kontakty (p_rod_cislo in char) is select * from kontakty 
            where ROD_CISLO = p_rod_cislo;

        CURSOR c_user (p_rod_cislo in char) is select * from OS_UDAJE 
            where ROD_CISLO = p_rod_cislo;
    begin
        for user in c_user(p_rod_cislo)
        LOOP
            DBMS_OUTPUT.PUT_LINE( user.meno || ' ' || user.priezvisko);
        end loop;

        for kontakt in c_kontakty(p_rod_cislo)
        LOOP
            DBMS_OUTPUT.PUT_LINE( kontakt.popis || ' ' || kontakt.kontakt);
        end loop;
    end;

end Student_package;
/

BEGIN
    Student_package.vypis('841106/3456');
end;
/

/**
 *  (a) Do vytvoreného package doplňte metódu na 
 *  stránkovanie zoznamu študentov, pričom maximálny 
 *  počet študentov nech je 3.
 */




