-- Pomocou SQL generujte príkazy na zrušenie linuxového konta všetkých zamestnancov, 
-- ktorí ukončili pracovný pomer v posledný mesiac, ak login je osobné číslo 
-- zamestnanca a syntax príkazu je: userdel login (3 b):
select 'userdel ' || rod_cislo 
from p_zamestnanec 
where dat_do is not null and extract(month from (add_months(sysdate,-1))) = extract(month from dat_do);

-- Pomocou SQL vygenerujte príkaz pomocou ktorého zrušíte všetky tabuľky používateľa Kmat. (3 b):
select 'DROP TABLE ' || username || '.' || table_name 
from users, tables 
where username LIKE 'Kmat';

-- Vygenerujte príkazy na drop všetkých FK z tabuľky p_prispevky. 
-- USER_CONSTRAINT(#constraint_name, table_name, constraint_type, r_constraint_name(FK) ) 
-- Ak constraint_type je typu CHAR(1) a môže nadobúdať hodnoty s nasledovným významom 
-- (P-primary key, R-foreign key, C-check, U-unique,...) (3 b):

-- Pomocou SQL vygenerujte príkazy na zamknutie kont všetkých študentov, ktorí nemajú zapísaný predmet v 
-- šk. roku 2005. (pomocou tabulky zoznam a systémovej tabuľky all_users) syntax prikazu: alter 
-- user login account lock; (3 b):
select 'alter user ' || login || ' account lock;' 
from zoznam
where not exists (select 'x' from zoznam where skrok<>'2005');

-- Pomocou sql vygenerujte alter prikaz na dropnutie triggerov z tabuľky p_zamestananec. (3 b):
select 'alter table p_zamestnanec drop trigger ' || trigger_name 
from user_triggers
where table_name = 'P_ZAMESTNANEC';

-- Pomocou SQL príkazu generujte príkazy na pridelenie práva "create any directory" 
-- všetkým užívateľom z predmetu II07 v šk. roku 2005. (3 b):
SELECT 'GRANT CREATE ANY DIRECTORY TO ' || login 
FROM os_udaje 
WHERE cis_predm = 'II07' AND EXTRACT(YEAR FROM skrok) = 2005;