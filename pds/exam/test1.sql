SET SERVEROUTPUT ON;

select * from os_udaje;
/

CREATE TYPE t_osoba IS OBJECT ( 
    rc char(11), 
    meno varchar2(20), 
    priezvisko varchar2(20) 
) NOT FINAL; 
/

CREATE TYPE T_zam UNDER t_osoba( 
    oc number, 
    pozicia varchar(10) 
);
/

CREATE TABLE zamestnanci OF t_zam;
/

insert into zamestnanci values ( 
    t_zam('765402/8877', 'Meno','Priezv', 123, 'sef'));
/
    
    
Select count(*) into K1 from p_poistenie JOIN p_odvod_platba using(id_poistenca); 
Select count(*) into K2 from p_poistenie JOIN p_odvod_platba using(id_poistenca) JOIN p_zamestnavatel ON ( id_platitela = ICO ); 
Select count(*) into K3 from p_poistenie JOIN p_odvod_platba using(id_poistenca) LEFT JOIN p_zamestnavatel ON ( id_platitela = ICO );
Select count(*) into K4 from p_poistenie JOIN p_odvod_platba using(id_poistenca) where id_platitela IN ( select ico from p_zamestnavatel);

select id_poistenca, dat_od from p_poistenie;  
select DISTINCT id_poistenca, dat_od from p_poistenie;  
select id_poistenca from p_poistenie;

declare type t_pole IS TABLE OF integer; 
pole t_pole; 

begin
    pole:=t_pole(10,20,30,40,50,60,70); 
    dbms_output.put_line(pole(pole.next(2)));
end;
/


declare type t_pole IS VARRAY(10) OF integer; 
pole t_pole; 
begin
    pole:=t_pole(10,20,30,40,50,60,70); 
    pole.delete(); 
    dbms_output.put_line(pole.limit); 
end;
/



--(1) END IF; 
--(2) HTP.P(priezv); 
--(3) CLOSE cur1; 
--(4) END LOOP; 
--(5) ELSE 
--(6)FETCH cur1 INTO priezv; 
--(7) EXIT; 
--(8) OPEN cur1; 
--(9) IF cur1%FOUND THEN 
--(10) LOOP

/*
(8) OPEN cur1; 
(10) LOOP
(6)FETCH cur1 INTO priezv; 
(9) IF cur1%FOUND THEN 
(2) HTP.P(priezv); 
(5) ELSE 
(7) EXIT; 
(1) END IF; 
(4) END LOOP; 
(3) CLOSE cur1; 
*/
    
--TODO
-- K jednotlivým krajom Slovenskej republiky a mesiacom prvého kvartálu 
-- roku 2008 vypíšte celkovú sumu odvedenú samoplatcami.

select * from p_odvod_platba
where EXTRACT(YEAR FROM obdobie) = 2016 AND EXTRACT(MONTH FROM obdobie) = 1;

SELECT n_kraja,
SUM(CASE WHEN EXTRACT(YEAR FROM obdobie) = 2016 AND EXTRACT(MONTH FROM obdobie) = 1 THEN suma ELSE 0 END) "1 mesiac",
SUM(CASE WHEN EXTRACT(YEAR FROM obdobie) = 2016 AND EXTRACT(MONTH FROM obdobie) = 2 THEN suma ELSE 0 END) "2 mesiac",
SUM(CASE WHEN EXTRACT(YEAR FROM obdobie) = 2016 AND EXTRACT(MONTH FROM obdobie) = 3 THEN suma ELSE 0 END) "3 mesiac"

FROM p_kraj 
JOIN p_okres USING (id_kraja) 
JOIN p_mesto USING (id_okresu) 
JOIN p_osoba USING (PSC) 
JOIN p_poistenie USING (rod_cislo) 
JOIN p_odvod_platba USING (id_poistenca)
--WHERE id_platitela IS NULL
GROUP BY n_kraja, id_kraja;

-- . Vypíšte názvy typov príspevkov, ktoré NEBOLI vyplácané minulý 
-- kalendárny mesiac. Použite EXISTS. (3 b):

SELECT popis
FROM p_typ_prispevku t 
WHERE NOT EXISTS (
    SELECT 'x' FROM p_prispevky p 
        WHERE t.id_typu = p.id_typu 
        AND EXTRACT(MONTH FROM obdobie) = EXTRACT(MONTH FROM ADD_MONTHS(SYSDATE, -1)) 
        AND EXTRACT(YEAR FROM obdobie) = EXTRACT(YEAR FROM SYSDATE)
);

-- Vypíšte kraje, kde býva viac žien ako mužov.

SELECT n_kraja FROM
    (SELECT n_kraja, 
    SUM(CASE WHEN SUBSTR(rod_cislo, 3, 1) = 0 OR SUBSTR(rod_cislo, 3, 1) = 1 THEN 1 ELSE 0 END) m,
    SUM(CASE WHEN SUBSTR(rod_cislo, 3, 1) = 5 OR SUBSTR(rod_cislo, 3, 1) = 6 THEN 1 ELSE 0 END) z
FROM p_kraj 
    JOIN p_okres USING (id_kraja) 
    JOIN p_mesto USING (id_okresu) 
    JOIN p_osoba USING (PSC)
GROUP BY n_kraja, id_kraja)
WHERE z > m;

select n_kraja from (
SELECT n_kraja, 
    SUM(CASE WHEN SUBSTR(rod_cislo, 3, 1) = 0 OR SUBSTR(rod_cislo, 3, 1) = 1 THEN 1 ELSE 0 END) m,
    SUM(CASE WHEN SUBSTR(rod_cislo, 3, 1) = 5 OR SUBSTR(rod_cislo, 3, 1) = 6 THEN 1 ELSE 0 END) z
FROM p_kraj 
    JOIN p_okres USING (id_kraja) 
    JOIN p_mesto USING (id_okresu) 
    JOIN p_osoba USING (PSC)
GROUP BY n_kraja, id_kraja

) where m > z;


-- 7. Horizontálne fragmentujte aspoň na 3 fragmenty reláciu 
-- P_PRISPEVKYpodľa typu príspevku. (2 b):

DEFINE FRAGMENT P1 AS SELECT * FROM P_PRISPEVKY WHERE id_typu = 1;
DEFINE FRAGMENT P2 AS SELECT * FROM P_PRISPEVKY WHERE id_typu = 2;
DEFINE FRAGMENT P3 AS SELECT * FROM P_PRISPEVKY WHERE id_typu > 2;
/

-- 16. Ku každému držiteľovi ZTP vypíšte sumu príspevkov, ktoré dostal za minulý kalendárny rok. (2 b):

SELECT meno, priezvisko, SUM(suma)
FROM p_osoba
    JOIN p_ZTP USING (rod_cislo) 
    JOIN p_poberatel USING (rod_cislo) 
    JOIN p_prispevky USING(id_poberatela)
WHERE EXTRACT(YEAR FROM obdobie) = EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12))
GROUP BY meno, priezvisko, rod_cislo
/

-- 23. K jednotlivým názvom zamestnávateľov a kvartálom minulého roka vypíšte počet prijatých osôb do zamestnania. (4 b):

SELECT nazov,
    SUM(CASE WHEN EXTRACT(MONTH FROM dat_od) = 1 OR EXTRACT(MONTH FROM dat_od) = 2 OR EXTRACT(MONTH FROM dat_od) = 3 THEN 1 ELSE 0 END) "1 kvartal",
    SUM(CASE WHEN EXTRACT(MONTH FROM dat_od) = 4 OR EXTRACT(MONTH FROM dat_od) = 5 OR EXTRACT(MONTH FROM dat_od) = 6 THEN 1 ELSE 0 END) "2 kvartal",
    SUM(CASE WHEN EXTRACT(MONTH FROM dat_od) = 7 OR EXTRACT(MONTH FROM dat_od) = 8 OR EXTRACT(MONTH FROM dat_od) = 9 THEN 1 ELSE 0 END) "3 kvartal",
    SUM(CASE WHEN EXTRACT(MONTH FROM dat_od) = 10 OR EXTRACT(MONTH FROM dat_od) = 11 OR EXTRACT(MONTH FROM dat_od) = 12 THEN 1 ELSE 0 END) "4 kvartal"
FROM p_zamestnavatel zl 
    JOIN p_zamestnanec zc ON (zc.id_zamestnavatela = zl.ICO)
WHERE EXTRACT(YEAR FROM dat_od) = EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -1))
GROUP BY nazov, ICO;

--24. Pre každý okres vypíšte osobu, ktorá bola poistencom najdlhšie. Ak bola evidovaná viackrát, intervaly sčítajte. (3 b):

SELECT n_okresu, meno, priezvisko 
    FROM (
        SELECT 
            n_okresu, 
            meno, 
            priezvisko, 
            RANK() OVER (PARTITION BY id_okresu ORDER BY trvanie DESC) poradie FROM (
                SELECT 
                    n_okresu, 
                    id_okresu, 
                    meno, 
                    priezvisko, 
                    SUM(NVL(dat_do, SYSDATE) - dat_od) trvanie
                FROM p_okres 
                    JOIN p_mesto USING (id_okresu) 
                    JOIN p_osoba USING (PSC) 
                    JOIN p_poistenie USING (rod_cislo)
                GROUP BY n_okresu, id_okresu, meno, priezvisko, rod_cislo
            )
    )
WHERE poradie = 1;

-- 26. Pomocou SQL príkazu generujte príkazy na pridelenie práva "create any directory" všetkým užívateľom z predmetu II07 v šk. roku 2005. (3 b):
SELECT 'GRANT CREATE ANYDIRECTORYTO ' || login FROM os_udaje WHERE cis_predm = 'II07' AND EXTRACT(YEAR FROM skrok) = 2005;


--32. Napíšte najvhodnejší index pre tabuľku P_ODVOD_PLATBA pre nasledovný dotaz: select rod_cislo, meno, priezvisko,

sum(suma) from p_osoba JOIN p_poistenie USING ( rod_cislo ) JOIN p_odvod_platba USING ( id_poistenca ) where
to_char(obdobie, 'YYYY') = 2016 group by rod_cislo, meno, priezvisko ;

CREATE INDEX IX1 ON p_odvod_platba(to_char(obdobie, 'YYYY'), suma);

-- 38. Pre jednotlivé mestá Nitrianskeho kraja vypíšte percentuálne rozloženie mužov a žien. (4 b):

SELECT  
    n_mesta, 
    (m / t * 100) || '%' Muzi, 
    (z / t * 100) || '%' Zeny 
FROM ( 
    SELECT 
        n_mesta,
        SUM(CASE WHEN SUBSTR(rod_cislo, 3, 1) = 0 OR SUBSTR(rod_cislo, 3, 1) = 1 THEN 1 ELSE 0 END) m,
        SUM(CASE WHEN SUBSTR(rod_cislo, 3, 1) = 5 OR SUBSTR(rod_cislo, 3, 1) = 6 THEN 1 ELSE 0 END) z,
        COUNT(*) t
    FROM p_kraj 
    JOIN p_okres USING (id_kraja) 
    JOIN p_mesto USING (id_okresu) 
    JOIN p_osoba USING (PSC)
WHERE n_kraja = 'Nitriansky'
GROUP BY n_mesta
);

-- 43. Vypíšte 5% obyvateľov s najväčšími odvodmi do poisťovne pre každé mesto osobitne. (3 b):

SELECT mesto, meno, priezvisko 
FROM (
    SELECT 
        n_mesta mesto, 
        meno, 
        priezvisko, 
        RANK() OVER (PARTITION BY n_mesta ORDER BY suma DESC) poradie
    FROM (
        SELECT n_mesta, meno, priezvisko, SUM(suma) suma
        FROM p_mesto 
            JOIN p_osoba USING (PSC) 
            JOIN p_poistenie USING (rod_cislo) 
            JOIN p_odvod_platba USING (id_poistenca)
        GROUP BY n_mesta, meno, priezvisko, rod_cislo
    )
)
WHERE poradie <= 0.05 * (
    SELECT COUNT(DISTINCT rod_cislo) 
        FROM p_mesto 
        JOIN p_osoba USING (PSC) 
        JOIN p_poistenie USING (rod_cislo) 
        JOIN p_odvod_platba USING (id_poistenca) 
    WHERE n_mesta = mesto
);

