---------------------------------------------------------------------
-- MAP
---------------------------------------------------------------------


DROP TABLE meals;
DROP TYPE food_ot FORCE;

--15
CREATE TYPE food_ot AS OBJECT (
    name VARCHAR2 (100), 
    food_group VARCHAR2 (100), 
    grown_in VARCHAR2 (100), 
    MAP MEMBER FUNCTION food_mapping
		 RETURN NUMBER
	)
NOT FINAL;
/

--16
CREATE OR REPLACE TYPE BODY food_ot 
IS 
   MAP MEMBER FUNCTION food_mapping 
      RETURN NUMBER 
   IS 
   BEGIN 
      RETURN dbms_random.value(-50, 50); 
   END; 
END;
/

--17
CREATE TABLE meals
(
	served_on	  DATE
 , main_course   food_ot
);
/

--18
BEGIN 
   -- Populate the meal table 
   INSERT INTO meals 
        VALUES (SYSDATE, food_ot ('Shrimp cocktail', 'PROTEIN', 'Ocean')); 
 
   INSERT INTO meals 
        VALUES (SYSDATE + 1, food_ot ('Stir fry tofu', 'PROTEIN', 'Wok')); 
 
   INSERT INTO meals 
           VALUES ( 
                     SYSDATE + 1, 
                     food_ot ('Peanut Butter Sandwich', 
                              'CARBOHYDRATE', 
                              'Kitchen')); 
 
   INSERT INTO meals 
           VALUES ( 
                     SYSDATE + 1, 
                     food_ot ('Brussels Sprouts', 'VEGETABLE', 'Backyard')); 
 
   COMMIT; 
END;
/

SELECT m.main_course.name name
	 FROM meals m
ORDER BY main_course;

---------------------------------------------------------------------
-- ORDER BY
---------------------------------------------------------------------


DROP TABLE meals;
DROP TYPE food_ot FORCE;



CREATE TYPE food_ot AS OBJECT
(
   name VARCHAR2 (100),
   food_group VARCHAR2 (100),
   ORDER MEMBER FUNCTION food_ordering (other_food_in IN food_ot)
      RETURN INTEGER
)
   NOT FINAL;
/
   
CREATE TYPE dessert_ot UNDER food_ot (
   contains_chocolate   CHAR (1)
 , year_created         NUMBER (4)
)
NOT FINAL;
/

CREATE TYPE cake_ot UNDER dessert_ot (
   diameter      NUMBER
 , inscription   VARCHAR2 (200)
);
/



CREATE OR REPLACE TYPE BODY food_ot
IS
   ORDER MEMBER FUNCTION food_ordering (other_food_in IN food_ot)
      RETURN INTEGER
   IS
   BEGIN
      RETURN dbms_random.value(-50, 50);
   END;
END;
/


CREATE TABLE meals
(
	served_on	  DATE
 , main_course   food_ot
);
/



BEGIN 
   -- Populate the meal table 
   INSERT INTO meals 
        VALUES (SYSDATE, food_ot ('Shrimp cocktail', 'PROTEIN')); 
 
   INSERT INTO meals 
        VALUES (SYSDATE + 1, food_ot ('Stir fry tofu', 'PROTEIN')); 
 
   INSERT INTO meals 
        VALUES (SYSDATE + 1, 
                dessert_ot ('Peanut Butter Sandwich', 
                            'CARBOHYDRATE', 
                            'N', 
                            1700)); 
 
   INSERT INTO meals 
        VALUES (SYSDATE + 1, food_ot ('Brussels Sprouts', 'VEGETABLE')); 
 
   INSERT INTO meals 
        VALUES (SYSDATE + 1, 
                cake_ot ('Carrot Cake', 
                         'VEGETABLE', 
                         'N', 
                         1550, 
                         12, 
                         'Happy Birthday!')); 
 
   COMMIT; 
END;
/

SELECT m.main_course.name name
	 FROM meals m
ORDER BY main_course;
/

------- others


declare type t_pole is table of integer;
    i integer;
    pole t_pole;
    j integer;
begin
    pole := t_pole(1,2,3,4,5,6,7,8);
    pole.delete(3);
    j := pole.first;
    for i in 1 .. pole.count loop
        dbms_output.put_line(pole(j));
        j := pole.next(j);
    end loop;
end;
/

select count(*) from p_poistenie pp where not exists (
    select 'x' from p_odvod_platba op where op.id_poistenca = pp.id_poistenca 
);

select count(*) from p_poistenie pp where exists (
    select 'x' from p_odvod_platba op where op.id_poistenca = pp.id_poistenca 
);

 