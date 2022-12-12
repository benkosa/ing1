-- Vypíšte osoby, ktoré nikdy neboli evidované ako telesne postihnuté.
SELECT count(*) from (
    SELECT
        DISTINCT ROD_CISLO, PRIEZVISKO, MENO
    FROM
        P_OSOBA
        left JOIN P_ZTP
        USING(ROD_CISLO)
    WHERE
        ID_POSTIHNUTIA != 1

);

SELECT count(*) from (
    select distinct rod_cislo,
                    meno,
                    priezvisko
    from p_osoba
            join p_ztp ztp using (rod_cislo)
    where not exists(
            select 'x'
            from p_typ_postihnutia pos
            where pos.id_postihnutia = ztp.id_postihnutia
            and nazov_postihnutia like 'Telesne Postihnutie'
    )
);

-- Ku každému kraju vypíšte 30% osôb s najväčšími odvodmi.
select * from (
    select 
        meno, 
        PRIEZVISKO,
        ROD_CISLO,
        (rank() over (partition by n_kraja order by sum(suma) desc)) as rozd,
        sum(suma) suma, 
        n_kraja
        from P_KRAJ 
        join P_OKRES USING(ID_KRAJA)
        join P_MESTO USING(ID_OKRESU)
        join P_OSOBA USING(PSC)
        join P_POISTENIE USING(rod_cislo)
        join P_ODVOD_PLATBA USING(ID_POISTENCA)
    group by meno, priezvisko, rod_cislo, n_kraja
) sel 
where rozd <= 0.30 * (
    select count(distinct rod_cislo) 
        from P_KRAJ 
        join P_OKRES USING(ID_KRAJA)
        join P_MESTO USING(ID_OKRESU)
        join P_OSOBA USING(PSC)
        join P_POISTENIE USING(rod_cislo)
        join P_ODVOD_PLATBA USING(ID_POISTENCA)
    WHERE N_KRAJA = sel.n_kraja
);

-- Vygenerujte príkazy Insert na vloženie dát do 
-- tabuľky p_osoba z tabuľky os_udaje v schéme priklad_db2.

select 'INSERT INTO p_osoba VALUES (' 
        || ROD_CISLO || ', ' 
        || MENO || ', ' 
        || PRIEZVISKO || ', ' 
        || PSC || ', ' 
        || ULICA || ' 
    );'
from priklad_db2.os_udaje 
where psc is not null and rod_cislo not in (select rod_cislo from p_osoba); 
--psc nesmie byt null a rovnake osoby uz nesmu byt v tabulke os_udaje kvolu duplicite kluca

-- Vytvorte objektový typ t_zviera, ktorý bude obsahovať 
-- aspoň 4 atribúty, konštruktor a metódu na triedenie.

create or replace type t_zviera as object (
    pocet number,
    nazov varchar2(30),
    druh  varchar2(30),
    rod   varchar2(30),
    constructor function t_zviera(
        pocet_par number, 
        nazov_par varchar2, 
        druh_par varchar2) return self as result,
    map member function triedenie return varchar2
);
/

-- Majme tabuľku objektov typu t_zviera. Vytvorte druhú 
-- tabuľku, kde bude objektový atribút typu t_zviera. 
-- Napíšte príkaz Select, pomocou ktorého vložíte dáta 
-- z existujúcej tabuľky do novej. 

create table objzvieratko of t_zviera;

desc objzvieratko;

insert into objzvieratko
values (5, 'ado', 'homoerektus', 'degesko');

insert into objzvieratko
values (5, 'jakub', 'homosapienssapiens', 'boss');

select * from objzvieratko;

create table zvieratko
(
    zviera t_zviera
);

desc zvieratko;

insert into zvieratko
select value(t)
from objzvieratko t;

select *
from zvieratko;

select k.zviera.pocet
from zvieratko k;

select value(s).pocet from objzvieratko s;

-- Vygenerujte a spustite príkazy na 
-- rekompiláciu všetkých Vašich indexov 
-- (user_indexes – table_name, index_name) => alter index index_name rebuild;

begin
    for riadok in (select 'alter index ' || index_name || ' rebuild' rebuild from user_indexes)
        loop
            execute immediate riadok.rebuild;
        end loop;
end;
/

select 'alter index ' || index_name || ' rebuild' rebuild
from user_indexes;

-- Vypíšte menný zoznam osôb, za ktoré nebolo nikdy platené 
-- poistenie (t.j. buď boli oslobodené od platenia poistného, 
-- alebo nemajú žiaden záznam v tabuľke p_poistenie).

select count (*) from (
    select distinct ROD_CISLO, meno, PRIEZVISKO
    from P_OSOBA
        left join P_POISTENIE USING(ROD_CISLO)
    WHERE ID_POISTENCA is null 
    or upper(oslobodeny) like 'A'
);

select count (*) from (
    select distinct os.ROD_CISLO, meno, priezvisko
    from p_osoba os
            left join p_poistenie po on po.rod_cislo = os.rod_cislo
    where not exists(select 'x' from p_poistenie pp where os.rod_cislo = pp.rod_cislo)
    or upper(oslobodeny) like 'A'
    group by meno, priezvisko, oslobodeny, os.rod_cislo
);


-- Vytvorte tabuľku teplôt, v ktorej budete uchovávať hodnotu 
-- teploty každú hodinu. Napíšte skript, ktorý vygeneruje teplotu 
-- pre 2 dni s tým, že rozdiel teplôt medzi susednými hodinami 
-- nebude viac ako 2 stupne. 


create table teplota
(
    teplota integer
);
drop table teplota;

declare
    teplotagen      integer;
    predoslateplota integer;
    timer           integer;
begin
    timer := 1;
    while timer <= 48
        loop
            if timer = 1 then
                teplotagen := dbms_random.value(-50, 50);
            else
                while abs(predoslateplota - teplotagen) > 2
                    loop
                        teplotagen := dbms_random.value(-50, 50);
                    end loop;
            end if;
            predoslateplota := teplotagen;
            insert into teplota values (teplotagen);
            timer := timer + 1;
        end loop;
end;

select *
from teplota;


create table teploty(
    teplota int,
    cas int
);

drop table teploty;

begin
    insert into teploty values(dbms_random.value(0,24),1);
    for t in 2..48 
    loop
        insert into teploty values((select teplota from teploty where cas = t - 1) + dbms_random.value(-1,1), t);
    end loop;
end;
/

-- Vypíšte štvrtú najvyššiu sumu zaplatenú na odvodoch pre každý štvrťrok tohto roku. 

select * from (
    select 
        suma, 
        to_char(dat_platby, 'yyyy') rok, 
        row_number() over (
            partition by (to_char(dat_platby, 'yyyy') || to_char(dat_platby, 'q')) 
            order by suma desc) poradie
    from p_odvod_platba
)
where poradie = 4 and rok = 2016;

-- Prostredníctvom kurzora vypíšte k jednotlivým osobám 
-- dobu, v ktorej boli evidovaní ako ZŤP. Ak osoba 
-- nebola nikdy evidovaná, tak vypíšte aspoň info o osobe. 

select 
    meno,
    priezvisko,
    cursor (
        select 
            round(nvl(dat_do, sysdate) - nvl(dat_od, sysdate),0) doma
        from p_ztp
        where p_osoba.rod_cislo = p_ztp.rod_cislo
    )
from p_osoba;



select "ID_filmu",
    null LINK_CLASS,
    apex_page.get_url(p_items => 'P27_ID_FILMU', p_values => "ID_filmu") LINK,
    null ICON_CLASS,
    null LINK_ATTR,
    null ICON_COLOR_CLASS,
    case when coalesce(:P27_ID_FILMU,'0') = "ID_filmu"
      then 'is-active' 
      else ' '
    end LIST_CLASS,
    (substr("nazov", 1, 50)||( case when length("nazov") > 50 then '...' else '' end )) LIST_TITLE,
    (substr("ID_filmu", 1, 50)||( case when length("ID_filmu") > 50 then '...' else '' end )) LIST_TEXT,
    null LIST_BADGE
from "Film" x
where (:P27_SEARCH is null
        or upper(x."nazov") like '%'||upper(:P31_SEARCH)||'%'
        or upper(x."ID_filmu") like '%'||upper(:P31_SEARCH)||'%'
    )
order by "nazov"

DECLARE
  l_cursor SYS_REFCURSOR;
BEGIN
  
  OPEN l_cursor FOR
    SELECT "ID_produktu" AS "id_produktu",
           "ID_ceny" AS "id_ceny",
           "nazov_produktu" AS "nazov_produktu"
    FROM   "Typ_obcerstvenia" join "Cennik" using ("ID_ceny");

  APEX_JSON.initialize_clob_output;

  APEX_JSON.open_object;
  APEX_JSON.write('zoznam_obcerstveni', l_cursor);
  APEX_JSON.close_object;

  DBMS_OUTPUT.put_line(APEX_JSON.get_clob_output);
  APEX_JSON.free_output;
END;
/