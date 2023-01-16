-- Vypíšte zamestnávateľov od 6. po 12. miesto na základe CELKOVEJ odvedenej 
-- sumy do poisťovne za svojich zamestnancov.
select 
ico,
sum(suma) sumsuma
from p_zamestnavatel
    join p_zamestnanec on (p_zamestnavatel.ico = p_zamestnanec.id_Zamestnavatela)
    join p_odvod_platba using (id_poistenca)
group by ico
order by sumsuma
offset 6 rows fetch next 6 rows only;

-- Vypíšte 30% najľudnatejších krajín . (3 b): (miest)
select 
    psc, 
    n_mesta, 
    count(1) pocet
from p_mesto
join p_osoba using (psc)
group by psc, n_mesta
order by pocet desc
fetch first 30 percent rows only;








