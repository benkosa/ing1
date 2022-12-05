drop table uniza_tab;

create table uniza_tab
(
  os_cislo integer primary key, 
  nadriadeny integer, 
  meno varchar(50), 
  priezvisko varchar(50), 
  org_zlozka varchar(50)
);

alter table uniza_tab add constraint uniza_fk foreign key (nadriadeny) references uniza_tab(os_cislo);

insert into uniza_tab
 values(1, null, 'Jan', 'Celko', 'rektorat');
 
insert into uniza_tab
 values(2, 1, 'Karol', 'Matiasko', 'Oddelenie pre inf. systemy');
 
insert into uniza_tab
 values(3, 1, 'Pavol', 'Rafajdus', 'Oddelenie vedy a vyskumu'); 
 
insert into uniza_tab
 values(4, 1, 'Andrej', 'Czan', 'Oddelenie pre rozvoj'); 
 
insert into uniza_tab
 values(5, 2, 'Dagmar', 'Komarek', 'Oddelenie pre inf. systemy');
 
insert into uniza_tab
 values(6, 2, 'Dasa', 'Zidekova', 'Oddelenie pre inf. systemy');
 
insert into uniza_tab
 values(7, 2, 'Irena', 'Kubinova', 'Oddelenie pre inf. systemy'); 
 
insert into uniza_tab
 values(8, 1, 'Emil', 'Krsak', 'dekanat FRI'); 

insert into uniza_tab
 values(9, 8, 'Viliam', 'Lendel', 'dekanat FRI'); 
 
insert into uniza_tab
 values(10, 8, 'Peter', 'Marton', 'dekanat FRI');
 
insert into uniza_tab
 values(11, 8, 'Michal', 'Kohani', 'dekanat FRI');

insert into uniza_tab
 values(12, 8, 'Vitaly', 'Levashenko', 'KI FRI'); 
 
insert into uniza_tab
 values(13, 12, 'Miroslav', 'Kvassay', 'KI FRI');  
 
insert into uniza_tab
 values(14, 12, 'Marek', 'Kvet', 'KI FRI');   
 
insert into uniza_tab
 values(15, 12, 'Michal', 'Kvet', 'KI FRI'); 
 
insert into uniza_tab
 values(16, 15, 'Roman', 'Ceresnak', 'KI FRI');   
 
insert into uniza_tab
 values(17, 15, 'Veronika', 'Salgova', 'KI FRI');   
 
insert into uniza_tab
 values(18, 15, 'Martina', 'Hrinova', 'KI FRI');  
 
insert into uniza_tab
 values(19, 8, 'Ludmila', 'Janosikova', 'KI FRI');  
 
insert into uniza_tab
 values(20, 8, 'Norbert', 'Adamko', 'KI FRI');  
 
insert into uniza_tab
 values(21, 20, 'Andrea', 'Galadikova', 'KI FRI');   

insert into uniza_tab
 values(22, 8, 'Jaroslav', 'Janacek', 'KI FRI');
 
insert into uniza_tab
 values(23, 8, 'Peter', 'Jankovic', 'KI FRI'); 
 
COMMIT;