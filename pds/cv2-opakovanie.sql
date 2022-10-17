/**
 *   Vytvorte funkciu Get_pocet_opakovani, ktorej vstupným 
 *   parametrom bude osobné číslo študenta, výsledkom bude 
 *   počet predmetov, ktoré opakoval (použite príkaz 
 *   Select...into). a.Otestujte pomocou vykonania funkcie 
 *   príkazom EXECUTE apomocou príkazu Select.
 */

SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION GET_POCET_OPAKOVANI(
   OS_CISLO_studenta IN NUMBER
) RETURN NUMBER IS
   N3 NUMBER;
BEGIN
   SELECT
   COUNT(*) into N3
FROM
   (
      SELECT
         ROD_CISLO
      FROM
         STUDENT
         JOIN ZAP_PREDMETY
         USING (OS_CISLO)
      WHERE
         OS_CISLO = OS_CISLO_studenta
      HAVING
         COUNT(OS_CISLO) > 1
      GROUP BY
         ROD_CISLO, OS_CISLO
   );
   RETURN N3;
END;
/


DECLARE
   N3 NUMBER(10);
BEGIN
   n3:= GET_POCET_OPAKOVANI('501567');
   DBMS_OUTPUT.PUT_LINE(n3);
END;
/

/**
 *  Vytvorte anonymný blok, ktorého parametrom bude číslo predmetu
 *  (vyžiadajte vrámci tela od používateľa). Na konzolu vypíštenázov 
 *  predmetu. Použite príkaz Select... into. 
 *     a. Otestujte pre predmet BI06, BI08.
 *     b. Vyriešte problém:
 *        i.    Definovaním výnimky
 *        ii.   TODO Prepisom príkazu Select...into na iný typ
 *        iii.  TODO abezpečením, aby príkaz Select ... into fungoval 
 *              správne pre akúkoľvek vstupnú hodnotu.
 */

DECLARE
   nazov_predmetu char(50);
BEGIN
   select NAZOV into nazov_predmetu from PREDMET where CIS_PREDM = 'BI06';
   DBMS_OUTPUT.PUT_LINE(nazov_predmetu);
   
   BEGIN
   select NAZOV into nazov_predmetu from PREDMET where CIS_PREDM = 'BI08';
   EXCEPTION
      WHEN NO_DATA_FOUND THEN
        nazov_predmetu := '';
    END;
    
   DBMS_OUTPUT.PUT_LINE(nazov_predmetu);
END;
/ 

/**
 *  Vytvorte procedúru, ktorá na konzolu vypíše ku každému študentovi
 *  jeho študijný priemer.
 *     a.Použite BULK COLLECT
 *     b.Použite typ kurzora OPEN, LOOP, ...
 *     c.Použite typ kurzora cez FOR (2x)
 */