SET SERVEROUTPUT ON;

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
 *   (b) V sqlplus vypíšte zoznam študentov a ak majú nejaký 
 *   kontakt, tak ich vypíšte aj s kontaktom
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

 *  (a) Do vytvoreného package doplňte metódu na 
 *  stránkovanie zoznamu študentov, pričom maximálny 
 *  počet študentov nech je 3.
 *  (b) Doplňte metódu na získanie nasledovnej a predchádzajúcej stránky.
 *  (c) Doplňte definíciu veľkosti strany.
 *  https://stackoverflow.com/questions/4552769/sql-rownum-how-to-return-rows-between-a-specific-range
 */
CREATE or replace package Student_package IS
PROCEDURE vypis(p_rod_cislo char);

v_page_number number := 0;
v_page_size number := 3;



cursor c_strankovanie is select * from (
    select os_udaje.*, ROWNUM r from OS_UDAJE
)
where r > v_page_size*v_page_number-v_page_size 
and r <= v_page_size*v_page_number;

procedure getPage(p_page number);
procedure getNextPage;
procedure getActualPage;
procedure getPreviousPage;
procedure setPageSize(p_page_size number);

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

    procedure getPage(p_page number) is 
    begin 
        v_page_number := p_page;

        for c_row in c_strankovanie
        LOOP
            DBMS_OUTPUT.PUT_LINE( c_row.meno || ' ' || c_row.priezvisko);
        end loop;
    end;

    procedure getNextPage is begin
        v_page_number:=v_page_number + 1;
        for c_row in c_strankovanie
        LOOP
            DBMS_OUTPUT.PUT_LINE( c_row.meno || ' ' || c_row.priezvisko);
        end loop;
    end;

    procedure getActualPage is begin 
        for c_row in c_strankovanie
        LOOP
            DBMS_OUTPUT.PUT_LINE( c_row.meno || ' ' || c_row.priezvisko);
        end loop;
    end;

    procedure getPreviousPage is begin
        if v_page_number > 0 THEN
            v_page_number:=v_page_number - 1;
        end if;

        for c_row in c_strankovanie
        LOOP
            DBMS_OUTPUT.PUT_LINE( c_row.meno || ' ' || c_row.priezvisko);
        end loop;
    end;

    procedure setPageSize(p_page_size number) is begin
        v_page_size := p_page_size;
    end;
end Student_package;
/

BEGIN
    --Student_package.vypis('841106/3456');
    --Student_package.getActualPage;
    Student_package.getPage(1);
    --Student_package.getNextPage;
    --Student_package.getPreviousPage;
    --Student_package.setPageSize(10);
end;
/

/**
 *  (a) Vytvorte kópiu tabuľky kontakty pomocou príkazu Select
 */

 CREATE TABLE kontakt2 AS SELECT * FROM kontakty;

 select * from kontakt2;

/**
 * (b) Zmeňte povinnosť atribútu rod_cislo v tabuľke kontakt2 na nepovinné
 *
 * https://www.oracletutorial.com/oracle-basics/oracle-not-null/
 */

desc kontakt2;


ALTER TABLE kontakt2 MODIFY ( ROD_CISLO null);

/**
 *  (c) Do tabuľky kontakt2 vložte dva riadky bez udania rodného čísla
 */


insert into kontakt2
values (4,'', 'email', 'test2@gmail.com');

insert into kontakt2
values (5,'', 'email', 'test3@gmail.com');

select * from kontakt2;

/**
 *  (d) Vypíšte počet osôb (z tabuľky os_udaje)
 */
 select count(*) from OS_UDAJE;

/**
 *  (d) Vypíšte počet osôb (z tabuľky os_udaje)
 */
 select count(*) from kontakt2;

 /**
  *  (f) Vypíšte počet rôznych rodných čísel v tabuľke kontakt2
  */

 select distinct ROD_CISLO from kontakt2;

-- null rodne cislo nepocita
 select count(distinct ROD_CISLO) from kontakt2;

-- asi jedno null rodne cislo pocita ako unikatne
 select count( * ) from  (
    select distinct rod_cislo from kontakt2
 );

 /**
  *  (g) Vypíšte počet študentov, ktorí majú nejaký kontakt (ak 
  *  majú viac kontaktov, počítajte ho iba raz)
  */

  select distinct(rod_cislo) from OS_UDAJE join KONTAKT2 using (rod_cislo);

 /**
  *  (h) Vypíšte počet študentov, ktorí nemajú žiaden kontakt
  *  (i) Odstráňte problém.
  */

  SELECT distinct rod_Cislo from kontakt2;

--v kontakty nieje null rc
select count (*) from (
  select DISTINCT ROD_CISLO from OS_UDAJE
  where ROD_CISLO not in (
    SELECT distinct rod_Cislo from kontakty
  )
);

--v kontakt2 je a asi ro robi problem
select count (*) from (
  select DISTINCT ROD_CISLO from OS_UDAJE
  where ROD_CISLO not in (
    SELECT distinct rod_Cislo from kontakt2
  )
);

-- neviem ci to chcel takto vyriest ale joinom sa stratila null hdontoa
select count (*) from (
  select DISTINCT ROD_CISLO from OS_UDAJE
  where ROD_CISLO not in (
    select distinct(rod_cislo) from OS_UDAJE join KONTAKT2 using (rod_cislo)
  )
);









