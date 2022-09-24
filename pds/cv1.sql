select table_name from tabs;
-- user_tables, user, all, dba

desc os_udaje;

select meno, priezvisko, pocet
from
(select meno, priezvisko, rod_cislo, count(cis_predm) as pocet
 from os_udaje LEFT JOIN student using(rod_cislo)
               LEFT JOIN zap_predmety using(os_cislo)
  group by meno, priezvisko, rod_cislo
   order by 3);
  -- group by meno, priezvisko, rod_cislo ;
   
select meno, priezvisko, os_cislo
 from os_udaje left join student using(rod_cislo)
  where priezvisko='Biely';
  
  
select meno, priezvisko, rod_cislo, count(cis_predm) as pocet
 from os_udaje LEFT JOIN student using(rod_cislo)
               LEFT JOIN zap_predmety using(os_cislo)
  group by meno, priezvisko, rod_cislo
   having count(cis_predm) = (select max(count(cis_predm))
                                from zap_predmety join student using(os_cislo)
                                 group by rod_cislo);
                                 
select meno, priezvisko, os_cislo
 from os_udaje LEFT join student using(rod_cislo);

select meno, priezvisko, os_cislo
 from os_udaje LEFT join student ON(os_udaje.rod_cislo=student.rod_cislo)
  order by os_cislo nulls first;

select meno, priezvisko, os_cislo
 from os_udaje LEFT join student ON(os_udaje.rod_cislo=student.rod_cislo
                                         and rocnik=2)
  order by os_cislo nulls last; 
  
select meno, priezvisko, os_cislo
 from os_udaje LEFT join student ON(os_udaje.rod_cislo=student.rod_cislo)
 where rocnik=2
  order by os_cislo nulls last;   
  
select meno, priezvisko
 from os_udaje
  where rod_cislo NOT IN (select rod_cislo from student union select null from dual);

select meno, priezvisko
 from os_udaje 
  where not exists(select 'x' from student 
                    where student.rod_cislo=os_udaje.rod_cislo);
                
select meno, priezvisko, count(os_cislo)
 from os_udaje left join student using(rod_cislo)
  group by meno, priezvisko, rod_cislo
   having count(os_cislo)<=2;

set serveroutput on;
   
begin
 for premenna in (select meno, priezvisko from os_udaje)
  loop
   dbms_output.put_line(premenna.meno);
  end loop;
end;
/

create user LOGIN identified by HESLO;
-- LOGIN - priezvisko
-- HESLO - krstne meno

select 'create user '|| priezvisko || ' identified by ' || meno
 from os_udaje;
 
create user Novak identified by Peter;  
drop user novak;

select table_name from tabs;
alter table NAZOV_TAB rename to NOVY_NAZOV;

select 'alter table '||table_name ||' rename to '|| 'tab'||table_name
 from tabs;
 
begin
 for premenna in (select 'alter table '||table_name 
                         ||' rename to '|| 'tab'||table_name prikaz
                    from tabs)
 loop
  execute immediate premenna.prikaz;
 end loop;
end;
/

begin
 for premenna in (select 'alter table '||table_name 
                         ||' rename to '|| substr(table_name,4) prikaz
                    from tabs)
 loop
  execute immediate premenna.prikaz;
 end loop;
end;
/

select table_name from tabs;

select meno, priezvisko,
       cursor(select os_cislo 
                from student 
                 where os_udaje.rod_cislo=student.rod_cislo)
from os_udaje;

select meno, priezvisko, 
      listagg(os_cislo, ' ') within group (order by os_cislo)
from os_udaje left join student using(rod_cislo)
 group by meno, priezvisko, rod_cislo;
 