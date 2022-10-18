/**
kurzory:
https://www.youtube.com/watch?v=sBdO_XhvUnk&ab_channel=yrrhelp
bulk:
https://www.youtube.com/watch?v=HPVzFBluM_Q&ab_channel=ManishSharma
*/

/** 1.
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

/** 2.
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

-- i.    Definovaním výnimky
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

/** 4.
 *  Vytvorte procedúru, ktorá na konzolu vypíše ku každému študentovi
 *  jeho študijný priemer.
 *     a.Použite BULK COLLECT
 *     b.Použite typ kurzora OPEN, LOOP, ...
 *     c.Použite typ kurzora cez FOR (2x)
 */
-- a.Použite BULK COLLECT
 DECLARE
   TYPE Array_Students is TABLE of INTEGER;
   arrayStudents Array_Students;

   Type Array_student_grade is table of char(1);
   arrayStudentGrades Array_student_grade;

   average number;

 BEGIN

   select OS_CISLO BULK COLLECT INTO arrayStudents
   from STUDENT;

   for i in 1..arrayStudents.count loop
      DBMS_OUTPUT.PUT_LINE(i || ' ' || arrayStudents(i));

      select VYSLEDOK bulk COLLECT into arrayStudentGrades
      from ZAP_PREDMETY 
      where OS_CISLO = arrayStudents(i) 
         and VYSLEDOK is not null;

      average := 0;
      for j in 1..arrayStudentGrades.count LOOP
         average := average + getZnamka(arrayStudentGrades(j));
      end loop;

      if arrayStudentGrades.count != 0 THEN
         DBMS_OUTPUT.PUT_LINE(average/arrayStudentGrades.count);
      end if;

   end loop;

 end;
 /

-- b.Použite typ kurzora OPEN, LOOP, ...
 declare
   cursor students is select * from STUDENT;
   studentsRow STUDENT%ROWTYPE;

   cursor grades (p_os_cislo in number)
   IS select *
      from ZAP_PREDMETY 
      where OS_CISLO = p_os_cislo 
         and VYSLEDOK is not null;
   gradesRow ZAP_PREDMETY%ROWTYPE;

   average number;

 begin

   open students;
   LOOP
      fetch students into studentsRow;
      exit when students%NOTFOUND;
      DBMS_OUTPUT.PUT_LINE(studentsRow.OS_CISLO);

      average := 0;
      open grades(studentsRow.OS_CISLO);
      LOOP
         fetch grades into gradesRow;
         exit when grades%NOTFOUND;
         average := average + getZnamka(gradesRow.VYSLEDOK);
      end loop;
      if grades%rowcount != 0 THEN
         DBMS_OUTPUT.PUT_LINE(average/grades%rowcount);
      end if;
      
      close grades;


   end loop;

   close students;

 END;
 /

-- c.Použite typ kurzora cez FOR (2x)
 declare
   cursor students is select * from STUDENT;

   cursor grades (p_os_cislo in number)
   IS select *
      from ZAP_PREDMETY 
      where OS_CISLO = p_os_cislo 
         and VYSLEDOK is not null;

   average number;
   gradesCount number;
 begin
   for studentsRow in students
   LOOP
      DBMS_OUTPUT.PUT_LINE(studentsRow.OS_CISLO);
      average := 0;
      gradesCount:= 0;

      for gradesRow in grades(studentsRow.OS_CISLO)
      LOOP
         average := average + getZnamka(gradesRow.VYSLEDOK);  
         gradesCount := gradesCount +1;     
      end loop;

      if gradesCount != 0 THEN
         DBMS_OUTPUT.PUT_LINE(average/gradesCount);
      end if;
   end loop;
 END;
 /

CREATE OR REPLACE Function getZnamka (znamka in char)
 return number
 AS
 BEGIN
   case znamka
      when 'A' then return 1;
      when 'B' then return 1.5;
      when 'C' then return 2;
      when 'D' then return 2.5;
      when 'E' then return 3;
      when 'F' then return 3.5;
   END CASE;
 end;
 /
