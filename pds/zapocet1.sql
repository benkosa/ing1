select * from STUDENT;

SELECT meno, priezvisko, count(*)
    from os_udaje LEFT join student using (rod_cislo)
        group by meno, priezvisko, rod_cislo
            having count(os_cislo) >= 2;


-- cv2 opakovanie
declare
    cursor c1
        is
        select skrok
        from predmet_bod
        group by skrok
        order by skrok;
    cursor c2 is select nazov, cis_predm, garant
                 from predmet
                          join predmet_bod using (cis_predm)
                 group by cis_predm, nazov, garant;
    cursor c3 is select meno, priezvisko, student.os_cislo, stav, st_zameranie, cis_predm, skrok
                 from os_udaje
                          join student using (rod_cislo)
                          join zap_predmety on (student.os_cislo = zap_predmety.os_cislo);
    counter  int;
    counter2 int;
begin
    counter := 1;
    counter2 := 1;
    for rocnik in c1
        loop
            dbms_output.put_line('Akademicky rok: ' || rocnik.skrok);
            for predmet in c2
                loop
                    dbms_output.put_line('Predmet: ' || rpad(counter, 5) || rpad(predmet.cis_predm, 10) ||
                                         rpad(predmet.nazov, 50) || 'Garant: ' ||
                                         predmet.garant);
                    for student in c3
                        loop
                            if (student.cis_predm = predmet.cis_predm and rocnik.skrok = student.skrok)
                            then
                                dbms_output.put_line('  ' || counter2 || '  ' || student.meno || ' ' || student.priezvisko);
                                counter2 := counter2 + 1;
                            end if;
                        end loop;
                    counter2 := 1;
                    counter := counter + 1;
                end loop;
            counter := 1;
        end loop;
end;