-- transakcie
    -- prakticke cvicenie

    -- undo logy sa snazim mat co najdlhsie
    -- bezpecne vymazat undo je pre transakicu ktora skoncila
    -- je dobre si ju 


create table pom_oc1 (
    os number
);

select * from os_udaje;

DESC STUDENT;

insert into pom_oc1 values(1); 
insert into pom_oc1 values(2);
       savepoint sp1;
insert into pom_oc1 values(3); 
insert into pom_oc1 values(4);
       savepoint sp2;
insert into pom_oc1 values(5); 
insert into pom_oc1 values(6);


select * from pom_oc1;

ROLLBACK to sp1;
ROLLBACK TO sp2;
ROLLBACK;
COMMIT;

-- sprava transakcii UNDO REDO

select * from student;

select * from student as of timestamp (sysdate - interval '1' hour);
-- tabulka sa nesmie strukturalne zmenit, pridanie atributov...


select log_mode from V$DATABASE;
-- v dolarove pohlady, niesu ulozne v databze, popisuju aktualny stav
-- ked nastane vypadok alebo havariua zmazu sa

-- vrati db o hoddinu naspat
-- to co sa stalo v db za poslednu hodinu som stratil
FLASHBACK table ZAP_PREDMETY to TIMESTAMP (SYSDATE - INTERVAL '1' hour );

-- DAT_DOpovoli row movement aby sme mohli vratit db o hodinu dozadu
alter table ZAP_PREDMETY ENABLE row MOVEMENT;







-- rekurzivne vztahy
desc uniza_tab;

select * from UNIZA_TAB;

-- kazdej osobe vypisat veduceho
select zamestnanec.meno zamestnanec_meno, zamestnanec.priezvisko zamestnanec_priezvisko, veduci.meno veduci_meno, veduci.priezvisko veduci_priezvisko
from UNIZA_TAB zamestnanec left join UNIZA_TAB veduci 
    on (ZAMESTNANEC.NADRIADENY = veduci.OS_CISLO);

-- kazdej osobe vypisat podriadeneho
select zamestnanec.meno zamestnanec_meno, zamestnanec.priezvisko zamestnanec_priezvisko, podriadeny.meno podriadeny_meno, podriadeny.priezvisko podriadeny_priezvisko
from UNIZA_TAB zamestnanec left join UNIZA_TAB podriadeny
    on (ZAMESTNANEC.OS_CISLO = PODRIADENY.NADRIADENY)
order BY zamestnanec.os_cislo;

-- pomocou agregacnej funkcie aby sme zobrazili do jedneho riadku
select zamestnanec.meno zamestnanec_meno, zamestnanec.priezvisko zamestnanec_priezvisko,
    LISTAGG(podriadeny.meno || ' ' || podriadeny.priezvisko || '(' || podriadeny.os_cislo || ')') WITHIN group
    (order by podriadeny.os_cislo)
from UNIZA_TAB zamestnanec left join UNIZA_TAB podriadeny
    on (ZAMESTNANEC.OS_CISLO = PODRIADENY.NADRIADENY)
group by zamestnanec.meno, zamestnanec.priezvisko, ZAMESTNANEC.OS_CISLO;

-- kazdej osobe kolegu
-- treba osterit ze osoba nemoze byt kolegom samemu sebe
select zamestnanec.meno zamestnanec_meno, zamestnanec.priezvisko zamestnanec_priezvisko, kolega.meno kolega_meno, kolega.priezvisko kolega_priezvisko
from UNIZA_TAB zamestnanec left join UNIZA_TAB kolega
    on (ZAMESTNANEC.NADRIADENY = kolega.NADRIADENY)
where ZAMESTNANEC.OS_CISLO <> KOLEGA.OS_CISLO;

-- pomocu agregacnej funkcie
-- treba osterit null hodnotu na korych s neda robit <>, aby nam nezmizli zamestnanci bez kolegov
select zamestnanec.meno , zamestnanec.priezvisko , 
(listagg(kolega.meno || ' ' || kolega.priezvisko || ', ') 
WITHIN GROUP (order by KOLEGA.OS_CISLO)) kolegovia
    from UNIZA_TAB zamestnanec left join UNIZA_TAB kolega
        on (ZAMESTNANEC.NADRIADENY = kolega.NADRIADENY)
    where ZAMESTNANEC.OS_CISLO <> KOLEGA.OS_CISLO or kolega.OS_CISLO is null -- podmienka 
group by zamestnanec.meno, zamestnanec.priezvisko, ZAMESTNANEC.OS_CISLO;

-- spracovanie celej hierarchyie

select level, OS_CISLO,  meno, priezvisko
    from UNIZA_TAB
    start WITH os_cislo= 1 -- koren hierarchye
    connect by prior OS_CISLO=NADRIADENY --sluzi na definiciu hierarchye
order by level;

-- krakjsie naformatovanie pomocou lpad
select lpad(' ', 2*level-2 ) || OS_CISLO || ' ' ||  meno  || ' ' || priezvisko
    from UNIZA_TAB
    start WITH os_cislo= 1 -- koren hierarchye
    connect by prior OS_CISLO=NADRIADENY --sluzi na definiciu hierarchye
order by level;

--osetrenie cyklu ked je clovek nadriadeny samemu sebe
select lpad(' ', 2*level-2 ) || OS_CISLO || ' ' ||  meno  || ' ' || priezvisko
    from UNIZA_TAB
    start WITH os_cislo= 1 -- koren hierarchye
    connect by prior OS_CISLO= case when OS_CISLO=NADRIADENY then -1 else NADRIADENY end
order by level;


-- indexovanie
desc osoba_tab;

select meno, PRIEZVISKO
    from OS_UDAJE
where meno = 'Peter';

create index ind1 on os_udaje(meno);

-- FULL (FAST FULL SCAN)
    --  ked treba prehladat vsetky data na ziskanie vysledku
    -- aby sme sa vyhli full prehladavaniu vytvorime index podla 
        -- atributov ktore chceme vyhladavat
        -- pozor na poradie
-- INDEX UNIQIE SCAN    
    -- ked vyhladavame podla unikatneho pk
-- BY INDEX ROWID
    -- ked treba nacitat ostatne hocnotu okrem vyhladavaneho kluca
 
--funkcionalne indexy

-- NESTED LOOPS
    -- ked netreba prehladavag celu tabulku vdaka obmedzeniu z where na pk
-- HASH JOIN
    -- ked treba prehladavat vsetky data lebo vo where nieje pk

-- hint syntax musi byt rpesna inak to je len komentar
select /*+INDEX(os_udaje ind1)*/ meno, PRIEZVISKO
FROM OS_UDAJE
    where meno = "Peter";


-- Valídne XMLko kedy je
-- čo robí hint nologging
-- potom si tam mal dve kokotiny s tranakciami
-- Dva selecty, jeden taký ten kde musíš robiť decode a sum, a druhý 37 až 78% ľudí, čo najdlhšie platia
-- Vytvoriť nový typ a objekt toho typu
-- máme karola a michala. Vytvoríme databázový link db_link inštancie databázy db_instance na serveri aterix. A potom bolo potrebné nalpniť tabuľku, ktorá nebola ani u karola ani u michala dátami z tabuľky karola.
-- Ešte jedno bol nested table - mal si updatnuť dáta v tabuľke a záznamy z nej presunúť do nested table


-- [ ] Vytvoriť DB link 
        CREATE DATABASE LINK dblink
        CONNECT TO remote_user IDENTIFIED BY password
        USING 'remote_database';

-- [ ] Niečo na transakcie 
-- [ ] 2 selekty 
-- [ ] Vymazať niečo z tabuľky a dať to do nestet table či čo, úplne som tomu nerozumel 
-- [ ] Potom že čo spraví/+append/ 
--        - kontrolné mechanizmy sa začínajú na konci vykonávania statementu
-- - [ ]  aku strukturu ma rowid 
-- •    The data object number of the object
-- •    The data block in the datafile in which the row resides
-- •    The position of the row in the data block (first row is 0)
-- •    The datafile in which the row resides (first file is 1). The file number is relative to the tablespace.
-- - [ ] Doplniť niečo do definície funkcie a to malo byť order 
-- - [ ] A ja som mal ešte že čo je to datový typ BAG
--         1. Set – A set is an unordered collection of elements without duplicates.
-- 2. Bags – A Bag is unordered collections of elements with duplicates. A bag is also called as MultiSets.
-- 3. Tables – It is an associations of keys and values

CREATE table pom (id number);

drop table pom;

Create or replace procedure proc1 AS 

PRAGMA AUTONOMOUS_TRANSACTION;
Begin 
       For I in 1..10 Loop
                Insert into pom values(i);
          End loop;
              Rollback;
End;
/

Create procedure proc2 As 
  Begin 
       Proc1;
        Insert into pom values(20);
  End;
/

Exec proc2;

rollback;

select * from pom;


--Aký výsledok vráti nasledovný príkaz Select?
--
--   Select count(*) from pom;
--  2. Pre jednotlivé rozpätia súm 0 - 2000, 2001-40000,40001 - 80000 
--     vypíšte percentuálne rozloženie osôb, ktorí túto čiastku celkovo 
--     dostali do poisťovne a sú z okresu Žilina.


create table pom as
select sum(suma) platby
from p_odvod_platba join p_poistenie using(id_poistenca)
join p_osoba using(rod_cislo)
join p_mesto using(PSC) where id_okresu = 'ZA'
group by rod_cislo;

drop table pom;

select * from pom;

select 'zilina',
(select count(platby) from pom where platby < 2001) as from0to2000,
(select count(platby) from pom where platby > 2000 and platby < 40001) as from2001to4000,
(select count(platby) from pom where platby > 40000 and platby < 80001) as from4001to8000
from dual;

drop pom;

--   3. Aktualizujte v tabuľke p_typ_prispevku historické záznamy z ceny 
--      500 eur na 700 pre typ s ID=12 a popisom=”testovanie systemu” .
--      (Záznam vkladajte do nested table p_historia).
--   4. Ak vzťah medzi tabuľkami osoba a zamestnanec je definovaný nasledovne:

ALTER TABLE zamestnanec
 ADD CONSTRAINT fk_z_o
 FOREIGN KEY (id_osoby)
 REFERENCES osoba(id_osoby)
 DEFERRABLE;

-- A tabuľka zamestnanec má primárny kľúč id_zamestnanca.
-- Session je nastavená ako:

   ALTER SESSION SET CONSTRAINTS = DEFERRED;

-- Do ktorých tabuliek budú dáta vložené po nasledovných insertoch 
-- (Predpokladáme, že tabuľky sú prázdne):

  insert into zamestnanec(id_zamestnanca, rod_cislo, dat_od,dat_do, id_poistenca)   values(1,’923456/1234’,'1.1.2010',’1.1.2016’, '88');
  insert into osoba(rod_cislo, meno, priezvisko,PSC,ulica) values(‘923456/1234’, 'Janko', 'Hrasko',’34243’,’Skolska’);
  
  -- 5. Vypíšte od 12 do 30 %  mužov (podľa priezviska), ktoré za posledné roky 
  -- odviedli do poisťovne 0 eur.
  -- 6. Aký výsledok bude vypísaný?
-- rollback sa vrati na posledny commit 
create table tab1(pom integer);
   insert into tab1 values(10);
   insert into tab1 values(20);
commit;
   delete from tab1;
   insert into tab1 values (30);
rollback;

select * from tab1;

drop table tab1;

-- Namiesto XXXXX doplňte vytvorenie objektu t_auto s ľubovoľnými hodnotami.
7. CREATE OR REPLACE TYPE t_auto AS OBJECT
(id_auta number,
 znacka varchar2(15),
 rok_vyroby number
);
/
DECLARE
	auto t_auto;
BEGIN
	auto := XXXXX;t_auto(1, 'mt', 2020)
END;
/
select max(pom) from tab1;

-- 8. Majme nasledujúci príkaz Select a k nemu index. Akú prístupovú metódu by ste zvolili. Svoje
-- rozhodnutie zdôvodnite:

Select meno, priezvisko
From os_udaje
Where rod_cislo=’561211/6347’;

-- Index s atribútmi v poradí: meno, priezvisko, rod_cislo
-- 9. Máme vytvorený databázový link db_link, ktorý sa odkazuje na inštanciu 
-- orcl_pdb servera asterix. Použitý používateľ je michal, ktorý má prístup ku 
-- všetkým tabuľkám celej inštancie.

-- Napíšte príkaz, ktorý do tabuľky data_tab používateľa karol na vzdialenom 
-- serveri vloží obsah lokálnej tabuľky p_ztp, tie, ktoré tam zatiaľ nie sú 
-- podľa hodnoty id_ztp. Predpokladajte, že tabuľka data_tab má rovnakú 
-- štruktúru ako tabuľka p_ztp.

-- 10. Kedy je vhodné využiť Skip scan ?






--39. K jednotlivým názvom krajom zo štátu Česko vypíšte percentuálne zloženie samoplatcom a klasických zamestnancov. (4 b):
select n_kraja,
count(case when poist.id_platitela is null then 0 else 1 end)/(count(poist.rod_cislo))*100 as samoplatca,
count(case when poist.id_platitela is not null then 0 else 1 end)/(count(poist.rod_cislo))*100 as zamestnanec
from p_kraj k
join p_krajina kr on(k.id_krajiny=kr.id_krajiny)
join p_okres o on(o.id_kraja = k.id_kraja)
join p_mesto m on(m.id_okresu = o.id_okresu)
join p_osoba os on(os.psc = m.psc)
join p_poistenie poist on(os.rod_cislo = poist.rod_cislo)
where n_krajiny = 'Cesko'
group by n_kraja;

select m.n_mesta,
count(case when substr(os.rod_cislo,3,1) ='1' then 1 else 0 end)/(count(rod_cislo))*100 as muzi,
count(case when substr(os.rod_cislo,3,1) = '5' then 1 else 0 end)/(count(rod_cislo))*100 as zeny
from p_mesto m join p_okres o using (m.id_okresu = m.id_okresu)
join p_kraj k on(o.id_kraja = k.id_kraja)
join p_osoba os on (os.psc = m.psc)
where k.n_kraja = 'Nitra'
where m.psc = os.psc
group by m.n_mesta;

select n_kraja,
count(case when poist.id_platitela is null then 0 else 1)/(count(poist.rod_cislo))*100 as samoplatca,
count(case when poist.id_platitela is not null then 0 else 1)/(count(poist.rod_cislo))*100 as zamestnanec
from p_kraj k
join p_krajina kr on(k.id_krajiny=kr.id_krajiny)
join p_okres o on(o.id_kraja = k.id_kraja)
join p_mesto m on(m.id_okresu = o.id_okresu)
join p_osoba os on(os.psc = m.psc)
join p_poistenie poist on(os.rod_cislo = poist.rod_cislo)
where n_krajiny = 'Cesko'
group by n_kraja;

select n_kraja,
count(case when poist.id_platitela is null then 0 else 1 end)/(count(poist.rod_cislo))100 as samoplatca,
count(case when poist.id_platitela is not null then 0 else 1 end)/(count(poist.rod_cislo))100 as zamestnanec
from p_kraj k
join p_krajina kr on(k.id_krajiny=kr.id_krajiny)
join p_okres o on(o.id_kraja = k.id_kraja)
join p_mesto m on(m.id_okresu = o.id_okresu)
join p_osoba os on(os.psc = m.psc)
join p_poistenie poist on(os.rod_cislo = poist.rod_cislo)
where n_krajiny = 'Cesko'
group by n_kraja;


select n_mesta,
sum(case sum(suma) < 2000 then 1 else 0 end)/sum(suma)*100,
sum(case sum(suma) > 2000 and sum(suma) < 40000 then 1 else 0 end)/sum(suma)*100,
sum(case sum(suma) > 40000 and sum(suma) < 80000 then 1 else 0 end)/sum(suma)*100
from p_mesto m join p_osoba o on (p.psc = value(o).psc) join p_poistenie p on (value(o).rod_cislo = p .rod_cislo) join
p_odvod_platba using(id_postenca)
group by n_mesta
having id_okresu='za';


create table tab1(pom integer);
   insert into tab1 values(10);
   insert into tab1 values(20);
commit;
   delete from tab1;
   insert into tab1 values (30);
rollback;

select * from tab1;

drop table tab1;

create table pom as
select sum(suma) platby
from p_odvod_platba join p_poistenie using(id_poistenca)
join p_osoba using(rod_cislo)
join p_mesto using(PSC) where id_okresu = 'ZA'
group by rod_cislo;

select 'zilina',
(select count(*) from pom where platby < 2001),
(select count(*) from pom where platby > 2000 and platby < 40001),
(select count(*) from pom where platby > 40000 and platby < 80001)
from dual;


select * from pom;

drop pom;


create table pom as
select sum(suma) platby, count(rod_cislo) pocet
from p_odvod_platba join p_poistenie using(id_poistenca)
join p_osoba using(rod_cislo)
join p_mesto using(PSC) where id_okresu = 'ZA'
group by rod_cislo;

drop table pom;

select * from pom;

select 'zilina',
(select count(pocet) from pom where platby < 2001)/(select sum(pocet) from pom where platby < 2001) as from0to2000,
(select count(pocet) from pom where platby > 2000 and platby < 40001)/(select sum(pocet) from pom where platby > 2000 and platby < 40001) as from2001to4000,
(select count(pocet) from pom where platby > 40000 and platby < 80001)/(select sum(pocet) from pom where platby > 40000 and platby < 80001) as from4001to8000
from dual;

select 'zilina',
(select count(pocet) from pom where platby < 2001) as from0to2000,
(select count(pocet) from pom where platby > 2000 and platby < 40001) as from2001to4000,
(select count(pocet) from pom where platby > 40000 and platby < 80001) as from4001to8000
from dual;

select 'zilina',
(select count(platby) from pom where platby < 2001) as from0to2000,
(select count(platby) from pom where platby > 2000 and platby < 40001) as from2001to4000,
(select count(platby) from pom where platby > 40000 and platby < 80001) as from4001to8000
from dual;


--12. Pre jednotlivé mestá Nitrianskeho kraja vypíšte percentuálne rozloženie mužov a žien. (4 b):
select m.n_mesta,
count(case when substr(os.rod_cislo,3,1) = '1' then 1 else 0 end)/(count(rod_cislo))*100 as muzi,
count(case when substr(os.rod_cislo,3,1) = '5' then 1 else 0 end)/(count(rod_cislo))*100 as zeny
from p_mesto m 
join p_okres o on (m.id_okresu = o.id_okresu)
join p_kraj k on(o.id_kraja = k.id_kraja)
join p_osoba os on (os.psc = m.psc)
where k.n_kraja = 'Nitriansky'
and m.psc = os.psc
group by m.n_mesta;
/

select substr(rod_cislo,3,1), rod_cislo, meno, priezvisko from p_osoba;



declare type t_pole is table of integer; 
pole t_pole; begin pole := t_pole(1,2,3,4,5,6,7,8);
pole.delete(2); 

    FOR I in 1 .. pole.count LOOP 
        if (pole.exists(i)) then 
            dbms_output.put_line(pole(i)); 
        end if; 
    END LOOP; 
    
end; 
/

SET SERVEROUTPUT ON;





