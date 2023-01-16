-- Vypíšte názvy typov príspevkov, ktoré NEBOLI vyplácané minulý 
-- kalendárny mesiac. Použite EXISTS. (3 b):

select popis 
from p_typ_prispevku t1
where not exists (
    select 'x' from p_prispevky t2 
    where to_char(obdobie, 'YYYYMM') = to_char(add_months(sysdate, -1), 'YYYYMM') and t1.id_typu = t2.id_typu
);

-- Pre jednotlivé mestá Nitrianskeho kraja vypíšte 
-- percentuálne rozloženie mužov a žien. (4 b):

select psc, 
case when m+z = 0 then 0 else m / (m+z) end muzi, 
case when m+z = 0 then 0 else z / (m+z) end zeny,
(m+z)
from (
    select 
        psc,
        sum(case when substr(rod_cislo, 3, 1) in ('1', '2') then 1 else 0 end) m,
        sum(case when substr(rod_cislo, 3, 1) in ('5', '6') then 1 else 0 end) z
    from p_okres
        join p_mesto using(id_okresu)
        join p_osoba using(psc)
    where id_kraja = 'NR'
    group by psc
);

select 
        psc,
        sum(case when substr(rod_cislo, 3, 1) in ('1', '2') then 1 else 0 end)/count(rod_cislo)*100 m,
        sum(case when substr(rod_cislo, 3, 1) in ('5', '6') then 1 else 0 end)/count(rod_cislo)*100 z,
        count(rod_cislo)
    from p_okres
        join p_mesto using(id_okresu)
        join p_osoba using(psc)
    where id_kraja = 'NR'
    group by psc;


    select 
        n_mesta, psc,
        sum(case when substr(rod_cislo,3,1) = 5 or substr(rod_cislo,3,1) = 6 then 1 else 0 end)/count(rod_cislo)*100 m,
        sum(case when substr(rod_cislo,3,1) = 0 or substr(rod_cislo,3,1) = 1 then 1 else 0 end)/count(rod_cislo)*100 z,
        count(rod_cislo)
    from p_okres
        join p_mesto using(id_okresu)
        join p_osoba using(psc)
    where id_kraja like 'NR'
    group by n_mesta, psc;


    select n_mesta,
        sum(case when substr(rod_cislo,3,1) = 5 or substr(rod_cislo,3,1) = 6 then 1 else 0 end)/count(rod_cislo) *100 as zeny,
        sum(case when substr(rod_cislo,3,1) = 0 or substr(rod_cislo,3,1) = 1 then 1 else 0 end)/count(rod_cislo) *100 as muzi
    from p_okres
        join p_mesto using(id_okresu)
        join p_osoba using(psc)
    where id_kraja like 'NR' 
    group by n_mesta;
    
-- Vypíšte ku každému okresu Trenčianskeho kraja počet mužov a žien, ktoré sa v narodili na Štedrý deň. (4 b):

select 
    m.n_mesta,
    sum(case when substr(os.rod_cislo,3,4) ='1224' then 1 else 0 end )as muzi,
    sum(case when substr(os.rod_cislo,3,4) ='6224' then 1 else 0 end )as zeny 
from p_mesto m 
    join p_okres o using (id_okresu) 
    join p_kraj k on (o.id_kraja = k.id_kraja)
    join p_osoba os on(os.psc = m.psc)
where k.n_kraja = 'Trencin' and m.psc = os.psc
group by m.n_mesta;

-- K jednotlivým názvom krajom zo štátu Česko vypíšte percentuálne zloženie samoplatcom a klasických zamestnancov. (4 b):

select 
    n_kraja,
    (sum (case when poist.id_platitela is null then 1 else 0 end)/count(poist.rod_cislo)) as samoplatca,
    (sum (case when poist.id_platitela is not null then 1 else 0 end)/count(poist.rod_cislo)) as zamestnanec 
from p_kraj k
    join p_krajina kr on(k.id_krajiny=kr.id_krajiny)
    join p_okres o on(o.id_kraja = k.id_kraja)
    join p_mesto m on(m.id_okresu = o.id_okresu)
    join p_osoba os on(os.psc = m.psc)
    join p_poistenie poist on(os.rod_cislo = poist.rod_cislo)
where n_krajiny = 'Cesko'
group by n_kraja;
