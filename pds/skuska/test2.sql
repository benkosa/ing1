-- K jednotlivým zamestnávateľom vypíšte počet zamestnancov a samoplatcov do 4O rokov. 
select 
    nazov,
    sum(1)
    from p_zamestnavatel zamestnavatel 
    join p_zamestnanec zamestnanec on (zamestnavatel.ico = zamestnanec.id_zamestnavatela)
    join p_poistenie using(rod_cislo)
where id_platitela is null 
    and
    to_date(
        SUBSTR(rod_cislo, 1, 2) 
        ||
        case when SUBSTR(rod_cislo, 3, 1) in (5, 6) then
            case when SUBSTR(rod_cislo, 3, 1) = 5 then
                '0'
            else
                '1'
            end
        else
            SUBSTR(rod_cislo, 3, 1)
        end
        || 
        SUBSTR(rod_cislo, 4, 3)
    , 'YYMMDD') > ADD_MONTHS(sysdate, 12*-40)
group by nazov;

--Vertikálne fragmentujte reláciu P_PRISPEVKY aspoň na 2 fragmenty. (2 b):
define fragment prisp_1 as select obdobie, id_poberatela id_typu from p_prispevky;
define fragment prisp_2 as select obdobie, id_poberatela, kedy, suma from p_prispevky; 


--Ku každému držiteľovi ZTP vypíšte sumu príspevkov, ktoré dostal za minulý kalendárny rok. (2 b):
select 
    rod_cislo,
    sum(suma)
from p_ztp 
    join p_poberatel using(rod_cislo)
    join p_prispevky using(id_poberatela)
where extract (year from obdobie) = extract(year from sysdate)-7
group by rod_cislo;

-- Pre jednotlivé rozpätia súm 0 - 2000, 2001-40000,40001 - 80000 
-- vypíšte počet osôb, ktorí túto čiastku celkovo odviedli do 
-- poisťovne a sú z okresu Žilina. (4 b):

select
    sum(case when sum(suma) >=0 and sum(suma) <= 2000 then 1 else 0 end) "0 - 2000",
    sum(case when sum(suma) > 2000 and sum(suma) <= 40000 then 1 else 0 end) "2001-40000",
    sum(case when sum(suma) > 40000 and sum(suma) <= 80000 then 1 else 0 end) "40001 - 80000"
from p_okres 
    join p_mesto using(id_okresu)
    join p_osoba using(psc)
    join p_poistenie using (rod_cislo)
    join p_odvod_platba using(id_poistenca)
where id_okresu = 'ZA'
group by rod_cislo;

-- Vypíšte 30% najľudnatejších krajín . (3 b):
select
    n_okresu,
    count(rod_cislo) "pludi"
from p_okres
    join p_mesto using (id_okresu)
    join p_osoba using (psc)
group by n_okresu
order by "pludi" desc
fetch first 30 percent rows only;

-- Pre jednotlivé rozpätia súm 0 - 2000, 2001-40000,40001 - 80000 vypíšte percentuálne 
-- rozloženie osôb, ktorí túto čiastku celkovo odviedli do poisťovne a sú z okresu Žilina. (4 b):

select 
"0-2000" / "all",
"2001-40000" / "all",
"40001-80000" / "all"
from (
    select
        sum(case when sum(suma) >=0 and sum(suma) <= 2000 then 1 else 0 end) "0-2000",
        sum(case when sum(suma) > 2000 and sum(suma) <= 40000 then 1 else 0 end) "2001-40000",
        sum(case when sum(suma) > 40000 and sum(suma) <= 80000 then 1 else 0 end) "40001-80000",
        sum(1) "all"
    from p_okres 
        join p_mesto using(id_okresu)
        join p_osoba using(psc)
        join p_poistenie using (rod_cislo)
        join p_odvod_platba using(id_poistenca)
    where id_okresu = 'ZA'
    group by rod_cislo
);

-- Pre každý typ postihnutia vypíšte 3 osoby podľa dĺžky poberanie daného príspevku. 
-- Ak osoba poberala daný príspevok viackrát, dobu spočítajte (p_ztp). (3 b):
declare
    cursor typy is select distinct id_typu from p_poberatel poberatel;
    
    cursor poberania (p_typ in number) is 
    select rod_cislo from (
            select 
                rod_cislo,
                sum(dat_do-dat_od) suma
            from p_poberatel cur_poberatel 
            where dat_do is not null and cur_poberatel.id_typu = p_typ
            group by rod_cislo
            order by suma desc
            fetch first 3 rows only
        );
begin
    for p_typ in typy loop
        DBMS_OUTPUT.PUT_LINE(p_typ.id_typu);
        for p_poberania in poberania(p_typ.id_typu) loop
            DBMS_OUTPUT.PUT_LINE(p_poberania.rod_cislo);
        end loop;
    end loop;
end;
/

select * from (
    select 
        id_postihnutia, 
        meno, 
        priezvisko, 
        nazov_postihnutia, 
        row_number() over (partition by id_postihnutia order by suma desc) rn
    from (
        select 
            id_postihnutia, 
            meno, 
            priezvisko, 
            nazov_postihnutia, 
            sum(case when dat_do is null then sysdate - dat_od else dat_do - dat_od end) as suma
        from p_typ_postihnutia 
        join p_ztp using(id_postihnutia)
        join p_osoba using(rod_cislo)
        group by id_postihnutia, meno, priezvisko, nazov_postihnutia
    )
) where rn <= 3;

SET SERVEROUTPUT ON;

-- Pre každú firmu vypíšte 3 zamestnancov, za ktorých sa zaplatilo minulý rok na odvodoch najviac.
select * from (
    select
        id_zamestnavatela,
        rod_cislo,
        row_number() over (partition by id_zamestnavatela order by suma desc) rn
    from (
        select 
            p_zamestnanec.rod_cislo,
            id_zamestnavatela,
            sum(suma) suma
        from p_zamestnanec 
        join p_poistenie using (id_poistenca)
        join p_odvod_platba using (id_poistenca)
        group by p_zamestnanec.rod_cislo, id_zamestnavatela
    )
) where rn <= 3;

declare
cursor firmy is select distinct ico from p_zamestnavatel;
cursor zamestnanci(firma in varchar) is
    select p_zamestnanec.rod_cislo,
        sum(suma)
    from p_zamestnanec 
    join p_poistenie using (id_poistenca)
    join p_odvod_platba using (id_poistenca)
    where id_zamestnavatela = firma
    group by p_zamestnanec.rod_cislo
    fetch first 3 rows only;
begin

for firma in firmy loop
    DBMS_OUTPUT.PUT_LINE(firma.ico);
    for zamestnanec in zamestnanci(firma.ico) loop
        DBMS_OUTPUT.PUT_LINE(zamestnanec.rod_cislo);
    end loop;
end loop;

end;
/

