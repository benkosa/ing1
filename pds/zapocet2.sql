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


