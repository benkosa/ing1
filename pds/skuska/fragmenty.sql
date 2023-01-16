-- Horizontálne fragmentujte aspoň na 3 fragmenty reláciu P_PRISPEVKY podľa typu príspevku. (2 b):
select * from p_prispevky where id_typu = 0;
select * from p_prispevky where id_typu = 1;
select * from p_prispevky where id_typu > 1;

-- Fragmentujte reláciu P_ODVOD_PLATBA na odvody zaplatené samoplatcami a odvody zamestnancov. (2 b):
select * from p_odvod_platba where id_poistenca in (select id_poistenca from p_zamestnanec);
select * from p_odvod_platba where id_poistenca not in (select id_poistenca from p_zamestnanec);
 
-- 2. Vertikálne fragmentujte reláciu P_PRISPEVKY aspoň na 2 fragmenty. (2 b):
select id_poberatela, obdobie, id_typu from P_PRISPEVKY;
select id_poberatela, obdobie, kedy, suma from P_PRISPEVKY;