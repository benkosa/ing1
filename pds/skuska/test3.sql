-- Ku každému držiteľovi ZTP vypíšte sumu príspevkov, ktoré dostal za minulý kalendárny rok.
select 
    rod_cislo, 
    sum(case when extract(year from obdobie) = extract (year from sysdate)-7 then suma else 0 end) suma
from p_ztp
    join p_poberatel using(rod_cislo)
    join p_prispevky using(id_poberatela)
group by (rod_cislo);

-- Pre každý typ postihnutia vypíšte 3 osoby podľa dĺžky poberania daného príspevku. Ak 
-- osoba poberala daný príspevok viackrát, dobu spočítajte (p_ztp). (3 b):
select * from(
    select 
        rod_cislo,
        id_typu,
        row_number() over (partition by id_typu order by suma desc) rn
    from (
        select 
            rod_cislo,
            p_poberatel.id_typu,
            sum(case when dat_do is null then sysdate-dat_od else dat_do-dat_od end) suma
        from p_poberatel
            join p_prispevky using(id_poberatela)
        group by rod_cislo, p_poberatel.id_typu
    )
) where rn <= 3;

-- K jednotlivým názvom zamestnávateľov a kvartálom minulého roka vypíšte počet prijatých osôb do zamestnania. (4 b):
select 
    ico,
    sum(case when extract(year from dat_od) = extract (year from sysdate)-7 and extract (month from dat_od) in ('1', '2', '3') then 1 else 0 end) kv1,
    sum(case when extract(year from dat_od) = extract (year from sysdate)-7 and extract (month from dat_od) in ('4', '5', '6') then 1 else 0 end) kv2,
    sum(case when extract(year from dat_od) = extract (year from sysdate)-7 and extract (month from dat_od) in ('7', '8', '9') then 1 else 0 end) kv3,
    sum(case when extract(year from dat_od) = extract (year from sysdate)-7 and extract (month from dat_od) in ('10', '11', '12') then 1 else 0 end) kv4
from p_zamestnavatel 
    join p_zamestnanec on (p_zamestnavatel.ico = p_zamestnanec.id_Zamestnavatela)
group by ico;

--  Pre každý okres vypíšte osobu, ktorá bola poistencom najdlhšie. Ak bola evidovaná viackrát, intervaly sčítajte. (3 b):
select * from (
    select
        rod_cislo,
        id_okresu,
        row_number() over (partition by id_okresu order by suma desc) rn
    from (
            
        select 
            rod_cislo,
            id_okresu,
            sum(case when dat_do is null then sysdate-dat_od else dat_do-dat_od end) suma
        from p_okres 
            join p_mesto using(id_okresu)
            join p_osoba using(psc)
            join p_poistenie using(rod_cislo)
        group by rod_cislo, id_okresu
    )
) where rn <= 1;

-- Vypíšte 5% obyvateľov s najväčšími odvodmi do poisťovne pre každé mesto osobitne. (3 b):

select 
    *
from (
    select
        rod_cislo,
        psc as cislo,
        row_number() over (partition by psc order by sum_suma desc )rn
    from (
        select 
            rod_cislo,
            psc,
            sum(suma) sum_suma
        from p_poistenie
            join p_odvod_platba using(id_poistenca)
            join p_osoba using(rod_cislo)
            join p_mesto using(psc)
        group by rod_cislo, psc
    )
) where rn <= 0.05 * (select count(*) from p_osoba where PSC = cislo);

-- Fragmentujte reláciu P_ODVOD_PLATBA na odvody zaplatené samoplatcami a odvody zamestnancov. (2 b):
select * from p_odvod_platba where id_poistenca in (select id_poistenca from p_zamestnanec);
select * from p_odvod_platba where id_poistenca not in (select id_poistenca from p_zamestnanec);























