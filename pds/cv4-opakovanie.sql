-- 1. vytvorte tabulku osoba_xml typu XMLTYPE :
-- https://docs.oracle.com/cd/A97630_01/appdev.920/a96620/xdb04cre.htm


CREATE TABLE osoba_xml2 of XMLTYPE;

desc osoba_xml2;

-- 2. vloˇzte asponˇ dva riadky (2 XML su ́bory) s nasledovnou ˇstruktu ́rou

INSERT INTO osoba_xml2 VALUES ( 
    XMLType(
        '<osoba rc="8014/2323">
            <meno>Jozko</meno>
            <priezvisko>Mrkvicka</priezvisko>
        </osoba>'
    )
);

INSERT INTO osoba_xml2 VALUES ( 
    XMLType(
        '<osoba rc="97010/82323">
            <meno>Fero</meno>
            <priezvisko>Joke</priezvisko>
        </osoba>'
    )
);

-- 3. vyp ́ıˇste obsah tabulky osoba xml
select * from osoba_xml;
-- 4. vyp ́ıˇste menny ́ zoznam osˆob



-- 5. zmenˇte priezvisko nejakej osoby

--zmeni vsetkym osobam
UPDATE osoba_xml
SET doc =  UPDATEXML(doc,
   '/osoba/priezvisko/text()', 'Joke2');

--zmeni osobe ktora sa vola 
UPDATE osoba_xml osoba
SET doc =  UPDATEXML(doc,
   '/osoba/priezvisko/text()', 'Gfhdfgh')
where osoba.doc.extract
    ('/osoba/meno/text()').getStringVal() = 'Jozko';


select * from osoba_xml osoba  where osoba.doc.extract
('/osoba/meno/text()').getStringVal() = 'Jozko';

-- 6. zmenˇte rodn ́e ˇc ́ıslo nejakej osoby

-- 7. vloˇzte osobu z osoba xml do tabulky os udaje


------------------------------------------------------
-- 1. vytvorte tabulku osoba_xml typu XMLTYPE :

CREATE TABLE osoba_xml2 of XMLTYPE;

desc osoba_xml;

-- 2. vloˇzte asponˇ dva riadky (2 XML su ́bory) s nasledovnou ˇstruktu ́rou

INSERT INTO osoba_xml VALUES ( 
    XMLType(
        '<osoba rc="8014/2323">
            <meno>Jozko</meno>
            <priezvisko>Mrkvicka</priezvisko>
        </osoba>'
    )
);

INSERT INTO osoba_xml VALUES ( 
    XMLType(
        '<osoba rc="9701082323">
            <meno>Fero</meno>
            <priezvisko>Joke</priezvisko>
        </osoba>'
    )
);

-- 3. vyp ́ıˇste obsah tabulky osoba xml
select * from osoba_xml2;

select value(s) from osoba_xml2 s;

select object_value from osoba_xml2;

select s.getClobVal() from osoba_xml2 s;

-- 4. vyp ́ıˇste menny ́ zoznam osˆob
-- 5. zmenˇte priezvisko nejakej osoby
Select count(*) 
  from osoba_xml2 s
  where EXISTSNODE(value(s), '/osoba/meno')=1;
  
select EXTRACT(value(s), '/osoba/meno')
 from osoba_xml2 s;
 



-- 6. zmenˇte rodn ́e ˇc ́ıslo nejakej osoby
-- 7. vloˇzte osobu z osoba xml do tabulky os udaje


