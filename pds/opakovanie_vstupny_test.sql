-- 1
-- Vypíšte koľko zamestnancov má zamestnávateľ Tesco.

SELECT
    COUNT(ROD_CISLO) AS POCET_ZAMESTNANCOV
FROM
    P_ZAMESTNANEC
    JOIN P_ZAMESTNAVATEL
    ON (P_ZAMESTNAVATEL.ICO = P_ZAMESTNANEC.ID_ZAMESTNAVATELA)
WHERE
    NAZOV = 'Tesco' and DAT_DO is not null;

-- 2
-- Ku každému veku vypíšte počet osôb, ktoré sú
-- oslobodené od poistenia. (Kazdú osobu
-- počítajte len raz)

SELECT
    SUBSTR(ROD_CISLO, 0, 2) AS ROK, COUNT(ROD_CISLO) AS POCET
FROM
    P_POISTENIE
WHERE
    OSLOBODENY LIKE 'a'
    OR OSLOBODENY LIKE 'A'
GROUP BY
    SUBSTR(ROD_CISLO, 0, 2);

-- 3
-- Vypíšte menný zoznam osôb, ktoré sú oslobodené od
-- platenia poistenia a nepoberajú žiaden príspevok.
SELECT
    MENO, PRIEZVISKO, PP.ROD_CISLO
FROM
    P_OSOBA
    JOIN P_POISTENIE PP2
    ON P_OSOBA.ROD_CISLO = PP2.ROD_CISLO JOIN P_POBERATEL PP
    ON P_OSOBA.ROD_CISLO = PP.ROD_CISLO
WHERE
    OSLOBODENY != 'n'
    AND OSLOBODENY != 'N'
    AND PP.DAT_DO IS NOT NULL
    OR PP.DAT_DO < SYSDATE
GROUP BY
    PP.ROD_CISLO, MENO, PRIEZVISKO;

-- 4
-- Ku každému človeku vypíšete sumu, ktorú zaplatila za
-- minulý kalendárny rok.
-- Ak nezaplatila nič, tak vypíšte aspoň jej meno a priezvisko.
SELECT
    MENO, PRIEZVISKO, SUMA
FROM
    P_OSOBA
    LEFT JOIN P_POISTENIE
    USING (ROD_CISLO)
    LEFT JOIN P_ODVOD_PLATBA POP
    ON P_POISTENIE.ID_POISTENCA = POP.ID_POISTENCA
WHERE
    EXISTS(
        SELECT
            'x'
        FROM
            P_ODVOD_PLATBA
        WHERE
            TO_CHAR(EXTRACT(YEAR FROM POP.DAT_PLATBY)) = 2017
    );

SELECT
    MENO, PRIEZVISKO, SUMA
FROM
    P_OSOBA
    LEFT JOIN P_POISTENIE
    USING (ROD_CISLO)
    LEFT JOIN P_ODVOD_PLATBA POP
    ON P_POISTENIE.ID_POISTENCA = POP.ID_POISTENCA
where TO_CHAR(EXTRACT(YEAR FROM POP.DAT_PLATBY)) = 2017;

-- 5
-- Ku každému človeku vypíšte aj meno jeho menovca
-- (majú zhodné priezviská, ale sú to dve osoby)
SELECT
    MENO, PRIEZVISKO, CURSOR(
    SELECT
        MENO AS MENOVEC
    FROM
        P_OSOBA MN
    WHERE
        PR.PRIEZVISKO = MN.PRIEZVISKO
        AND PR.ROD_CISLO <> MN.ROD_CISLO)
    FROM
        P_OSOBA PR;

-- 6
-- Vypíšte zoznam osôb, ktoré súčasne poberajú viac
-- ako jeden typ príspevku.
SELECT
    MENO, PRIEZVISKO, P_OSOBA.ROD_CISLO
FROM
    P_OSOBA
    JOIN P_POBERATEL PP
    ON P_OSOBA.ROD_CISLO = PP.ROD_CISLO
WHERE
    DAT_DO IS NULL
HAVING
    COUNT(ID_TYPU) > 1
GROUP BY
    P_OSOBA.ROD_CISLO, MENO, PRIEZVISKO;

-- 7
-- Vypíšte zoznam osôb a počet typov príspevkov,
-- ktoré poberajú/poberali.
-- Jeden typ príspevku aj keď v iných obdobiach počítajte iba raz.
SELECT
    MENO, PRIEZVISKO, ROD_CISLO, COUNT(DISTINCT(ID_TYPU))
FROM
    P_OSOBA
    JOIN P_POBERATEL
    USING(ROD_CISLO)
GROUP BY
    MENO, PRIEZVISKO, ROD_CISLO;

-- 8
-- Vypíšte zoznam osôb, ktoré boli/sú súčasne viackrát poistencami.
-- (Riešte aj prekrívanie intervalov)
SELECT
    OS.MENO, OS.PRIEZVISKO
FROM
    P_OSOBA OS
    JOIN P_POISTENIE PS1
    ON(PS1.ROD_CISLO = OS.ROD_CISLO) JOIN P_POISTENIE PS2
    ON(PS2.ROD_CISLO = OS.ROD_CISLO)
WHERE
    PS1.ROD_CISLO = PS2.ROD_CISLO
    AND PS1.DAT_OD <= PS2.DAT_DO
    AND PS1.DAT_DO >= PS2.DAT_OD
HAVING
    COUNT(os.ROD_CISLO) > 1
GROUP BY
    OS.ROD_CISLO, MENO, PRIEZVISKO;

--9
SELECT
    *
FROM
    P_OSOBA
    JOIN P_POISTENIE
    USING(ROD_CISLO) LEFT JOIN P_ODVOD_PLATBA
    USING(ID_POISTENCA)
WHERE
    DAT_DO IS NULL
    AND (DAT_PLATBY < ADD_MONTHS(SYSDATE, -6));

--10
SELECT
    EXTRACT(MONTH
FROM
    DAT_PLATBY) AS MESIAC, SUM(SUMA)
FROM
    P_ODVOD_PLATBA
WHERE
    EXTRACT(YEAR FROM DAT_PLATBY) = EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12))
GROUP BY
    EXTRACT(MONTH FROM DAT_PLATBY)
ORDER BY
    MESIAC;

SELECT
    CASE
        WHEN SUBSTR(P_OSOBA.ROD_CISLO, 1, 2) <= 22 THEN
            2022 - TO_NUMBER('20'
                || SUBSTR(P_OSOBA.ROD_CISLO, 1, 2))
        ELSE
            2022 - TO_NUMBER('19'
                || SUBSTR(P_OSOBA.ROD_CISLO, 1, 2))
    END AS VEK, COUNT(DISTINCT(P_POISTENIE.ROD_CISLO))
FROM
    P_OSOBA
    JOIN P_POISTENIE
    ON(P_OSOBA.ROD_CISLO=P_POISTENIE.ROD_CISLO)
WHERE
    LOWER(OSLOBODENY)='a'
GROUP BY
    SUBSTR(P_OSOBA.ROD_CISLO, 1, 2)
ORDER BY
    VEK;

-- 1. Vypísať počet ľudí čo sa narodili v jednotlivých
-- kvartáloch roka.

SELECT
    COUNT(ROD_CISLO), TO_CHAR(TO_DATE('01.' || MOD(SUBSTR(ROD_CISLO, 3, 2), 50) || '.1999', 'DD.MM.YYYY'), 'Q') AS KVARTAL
FROM
    P_OSOBA
GROUP BY
    TO_CHAR(TO_DATE('01.'
        || MOD(SUBSTR(ROD_CISLO, 3, 2), 50)
        || '.1999', 'DD.MM.YYYY'), 'Q')
ORDER BY
    KVARTAL;

-- 2. Vypísať zamestnancov čo začali a skončili pracovný
-- pomer v rovnakom mesiaci a roku.

SELECT
    P_OSOBA.MENO, P_OSOBA.PRIEZVISKO
FROM
    P_OSOBA
    JOIN P_ZAMESTNANEC
    ON (P_OSOBA.ROD_CISLO = P_ZAMESTNANEC.ROD_CISLO)
WHERE
    P_ZAMESTNANEC.DAT_DO IS NOT NULL
    AND TO_CHAR(P_ZAMESTNANEC.DAT_OD, 'MM') = TO_CHAR(P_ZAMESTNANEC.DAT_DO, 'MM')
    AND TO_CHAR(P_ZAMESTNANEC.DAT_OD, 'YYYY') = TO_CHAR(P_ZAMESTNANEC.DAT_DO, 'YYYY');

-- 3. Vypísať zamestnávateľov čo neplatil odvody pre
-- ZTP osoby, čo majú ZTP s názvom: duševná porucha

SELECT
    DISTINCT NAZOV
FROM
    P_ZAMESTNAVATEL
    JOIN P_PLATITEL
    ON (P_ZAMESTNAVATEL.ICO = P_PLATITEL.ID_PLATITELA) JOIN P_POISTENIE
    USING (ID_PLATITELA)
WHERE
    P_POISTENIE.ID_POISTENCA NOT IN (
        SELECT
            DISTINCT ID_POISTENCA
        FROM
            P_OSOBA
            JOIN P_POISTENIE
            USING (ROD_CISLO) JOIN P_ZTP
            USING (ROD_CISLO)
            JOIN P_TYP_POSTIHNUTIA
            USING (ID_POSTIHNUTIA)
        WHERE
            NAZOV_POSTIHNUTIA = 'Mentalne/Dusevne Postihnutie'
    );

-- 4. Vypísať meno, priezvisko rod číslo pre všetky
-- osoby, ak je osoba ZTP vypísať jej ID a dátum od
-- kedy jej ZTP, vypísať aj osoby ktoré nie sú ZTP

SELECT
    MENO, PRIEZVISKO, ROD_CISLO, ID_ZTP, DAT_OD
FROM
    P_OSOBA
    LEFT JOIN P_ZTP
    USING (ROD_CISLO);

--neplatili odvody a dusevna porucha
SELECT
    NAZOV
FROM
    P_ZAMESTNAVATEL ZAM
WHERE
    NOT EXISTS (
        SELECT
            'x'
        FROM
            P_POISTENIE
            JOIN P_ZTP
            USING(ROD_CISLO) JOIN P_TYP_POSTIHNUTIA
            USING(ID_POSTIHNUTIA)
        WHERE
            ID_PLATITELA = ZAM.ICO
            AND NAZOV_POSTIHNUTIA = 'dusevna porucha'
    );

-- Ku kaûdÈmu okresu vypÌöte jeho ID, n·zov a zoznam vöetk˝ch miest v ?om.
-- VypÌöte aj tie okresy, ku ktor˝m neevidujeme ûiadne mest·.
SELECT
    ID_OKRESU, N_OKRESU, CURSOR(
    SELECT
        N_MESTA
    FROM
        P_MESTO
    WHERE
        P_MESTO.ID_OKRESU = P_OKRES.ID_OKRESU)
    FROM
        P_OKRES
    GROUP BY
        ID_OKRESU, N_OKRESU;

SELECT
    'grant select on ' || TABLE_NAME || ' to brescher'
FROM
    TABS;

SELECT
    *
FROM
    BRESCHER.P_PLATITEL;

SELECT
    'revoke select on ' || TABLE_NAME || ' from brescher'
FROM
    TABS;

GRANT SELECT ON OS_UDAJE TO BIKESHARING;