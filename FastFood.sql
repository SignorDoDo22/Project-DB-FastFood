-- *********************************************
-- * SQL MySQL generation
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2
-- * Generator date: Sep 14 2021
-- * Generation date: Thu Aug  6 08:31:44 2026
-- * LUN file: C:\Users\baril\Desktop\Base di Dati\Scheletro P.D.B -  Prodotto.lun
-- * Schema: Schema_Rel_FastFood
-- *********************************************


-- Database Section
-- ________________

DROP DATABASE IF EXISTS schema_rel_fastfood;
CREATE DATABASE IF NOT EXISTS schema_rel_fastfood;
CREATE USER IF NOT EXISTS 'appuser'@'localhost'
IDENTIFIED BY 'StrongP@ssw0rd';
GRANT ALL PRIVILEGES ON schema_rel_fastfood.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
USE schema_rel_fastfood;

-- Tables Section
-- _____________

create table Categoria (
     IDCategoria char(50) not null,
     Nome char(50) not null,
     constraint ID_Categoria_ID primary key (IDCategoria));

create table Cliente (
     Telefono char(35) not null,
     Codice_Utente varchar(50) not null,
     Username char(50) not null,
     Password char(50) not null,
     Email char(50) not null,
     Nome char(50) not null,
     Cognome char(50) not null,
     Data_di_Nascita date not null,
     constraint ID_Cliente_ID primary key (Codice_Utente),
     constraint SID_Cliente_ID unique (Email));

create table ComponenteMenuOrdinato (
     Codice_Ordine varchar(50) not null,
     CodiceRiga char(29) not null,
     NumRowCompMenu bigint not null,
     Codice_Prodotto char(50) not null,
     constraint ID_ComponenteMenuOrdinato_ID primary key (Codice_Ordine, CodiceRiga, NumRowCompMenu));

create table CompostoMenu (
     Codice_Menu char(50) not null,
     Codice_Prodotto char(50) not null,
     quantita bigint not null,
     constraint ID_CompostoMenu_ID primary key (Codice_Menu, Codice_Prodotto));

create table Comprende (
     Codice_Ingrediente varchar(50) not null,
     Codice_Prodotto char(50) not null,
     Quantita numeric not null,
     constraint ID_Comprende_ID primary key (Codice_Ingrediente, Codice_Prodotto));

create table Ingrediente (
     Vegano boolean not null,
     Glutine boolean not null,
     Lattosio boolean not null,
     Nome_Ingrediente char(50) not null,
     Codice_Ingrediente varchar(50) not null,
     constraint ID_Ingrediente_ID primary key (Codice_Ingrediente));

create table Menu (
     Codice_Prodotto char(50) not null,
     constraint FKPro_Men_ID primary key (Codice_Prodotto));

create table ModificaComponenteMenu (
     Codice_Ordine varchar(50) not null,
     CodiceRiga char(29) not null,
     NumRowCompMenu bigint not null,
     Codice_Ingrediente varchar(50) not null,
     Quantita numeric not null,
     Tipo char(35) not null,
     constraint ID_ModificaComponenteMenu_ID primary key (Codice_Ordine, CodiceRiga, NumRowCompMenu, Codice_Ingrediente));

create table ModificaProdottoSingolo (
     Codice_Ordine varchar(50) not null,
     CodiceRiga char(29) not null,
     Codice_Ingrediente varchar(50) not null,
     Quantita numeric not null,
     Tipo char(35) not null,
     constraint ID_ModificaProdottoSingolo_ID primary key (Codice_Ingrediente, Codice_Ordine, CodiceRiga));

create table Ordine (
     DataCreazione date not null,
     Ind_Via char(50) not null,
     Ind_Citta char(50) not null,
     Ind_Civico char(50) not null,
     Codice_Ordine varchar(50) not null,
     Codice_Rider varchar(50),
     Codice_Utente varchar(50) not null,
     constraint ID_Ordine_ID primary key (Codice_Ordine));

create table Prodotto (
     Disponibile char not null,
     Codice_Prodotto char(50) not null,
     Nome_Prodotto char(50) not null,
     Prezzo_originario float(29) not null,
     Descrizione_Prodotto char(200) not null,
     Singolo char(50),
     Menu char(50),
     IDCategoria char(50) not null,
     constraint ID_Prodotto_ID primary key (Codice_Prodotto));

create table Recensione (
     Codice_Ordine varchar(50) not null,
     Voto_Rider decimal(2,1) not null,
     Testo_Recensione char(200) not null,
     Voto_Ordine decimal(3,2) not null,
     constraint FKCorrisponde_ID primary key (Codice_Ordine));

create table Rider (
     Telefono char(35) not null,
     Codice_Rider varchar(50) not null,
     Username char(50) not null,
     Password char(50) not null,
     Email char(50) not null,
     Nome char(50) not null,
     Cognome char(50) not null,
     Data_di_Nascita date not null,
     RaitingMedioRider decimal(2,1) not null,
     Guadagno float(29) not null,
     constraint ID_Rider_ID primary key (Codice_Rider),
     constraint SID_Rider_ID unique (Email));

create table Riga_prodotto (
     Codice_Ordine varchar(50) not null,
     CodiceRiga char(29) not null,
     Quantita decimal(3,2) not null,
     Prezzo float(29) not null,
     RigaProdottoMenu varchar(50),
     RigaProdottoSingolo varchar(50),
     constraint ID_Riga_prodotto_ID primary key (Codice_Ordine, CodiceRiga));

create table RigaProdottoMenu (
     Codice_Ordine varchar(50) not null,
     CodiceRiga char(29) not null,
     Codice_Prodotto char(50) not null,
     constraint FKRig_Rig_1_ID primary key (Codice_Ordine, CodiceRiga));

create table RigaProdottoSingolo (
     Codice_Ordine varchar(50) not null,
     CodiceRiga char(29) not null,
     Codice_Prodotto char(50) not null,
     constraint FKRig_Rig_ID primary key (Codice_Ordine, CodiceRiga));


create table Singolo (
     Codice_Prodotto char(50) not null,
     constraint FKPro_Sin_ID primary key (Codice_Prodotto));

create table Stato_Ordine (
     Codice_Ordine varchar(50) not null,
     Tempo time not null,
     Stato char(40) not null,
     constraint ID_Stato_Ordine_ID primary key (Codice_Ordine, Tempo, Stato));


-- Constraints Section
-- ___________________

-- Not implemented
-- alter table Categoria add constraint ID_Categoria_CHK
--     check(exists(select * from Prodotto
--                  where Prodotto.IDCategoria = IDCategoria));

alter table ComponenteMenuOrdinato add constraint FKRiferisceCompMenu_FK
     foreign key (Codice_Prodotto)
     references Singolo (Codice_Prodotto);

alter table ComponenteMenuOrdinato add constraint FKPrevede
     foreign key (Codice_Ordine, CodiceRiga)
     references RigaProdottoMenu (Codice_Ordine, CodiceRiga);

alter table CompostoMenu add constraint FKCom_Sin_1_FK
     foreign key (Codice_Prodotto)
     references Singolo (Codice_Prodotto);

alter table CompostoMenu add constraint FKCom_Men
     foreign key (Codice_Menu)
     references Menu (Codice_Prodotto);

alter table Comprende add constraint FKCom_Sin_FK
     foreign key (Codice_Prodotto)
     references Singolo (Codice_Prodotto);

alter table Comprende add constraint FKCom_Ing
     foreign key (Codice_Ingrediente)
     references Ingrediente (Codice_Ingrediente);

-- Not implemented
-- alter table Menu add constraint FKPro_Men_CHK
--     check(exists(select * from CompostoMenu
--                  where CompostoMenu.Codice_Menu = Codice_Prodotto));

alter table Menu add constraint FKPro_Men_FK
     foreign key (Codice_Prodotto)
     references Prodotto (Codice_Prodotto);

alter table ModificaComponenteMenu add constraint FKMod_Ing_1_FK
     foreign key (Codice_Ingrediente)
     references Ingrediente (Codice_Ingrediente);

alter table ModificaComponenteMenu add constraint FKMod_Com
     foreign key (Codice_Ordine, CodiceRiga, NumRowCompMenu)
     references ComponenteMenuOrdinato (Codice_Ordine, CodiceRiga, NumRowCompMenu);

alter table ModificaProdottoSingolo add constraint FKMod_Ing
     foreign key (Codice_Ingrediente)
     references Ingrediente (Codice_Ingrediente);

alter table ModificaProdottoSingolo add constraint FKMod_Rig_FK
     foreign key (Codice_Ordine, CodiceRiga)
     references RigaProdottoSingolo (Codice_Ordine, CodiceRiga);

-- Not implemented
-- alter table Ordine add constraint ID_Ordine_CHK
--     check(exists(select * from Stato_Ordine
--                  where Stato_Ordine.Codice_Ordine = Codice_Ordine));

-- Not implemented
-- alter table Ordine add constraint ID_Ordine_CHK
--     check(exists(select * from Riga_prodotto
--                  where Riga_prodotto.Codice_Ordine = Codice_Ordine));

alter table Ordine add constraint FKPrende_in_carico_FK
     foreign key (Codice_Rider)
     references Rider (Codice_Rider);

alter table Ordine add constraint FKCrea_FK
     foreign key (Codice_Utente)
     references Cliente (Codice_Utente);

alter table Prodotto add constraint EXTONE_Prodotto
     check((Singolo is not null and Menu is null)
           or (Singolo is null and Menu is not null));

alter table Prodotto add constraint FKAppartiene_FK
     foreign key (IDCategoria)
     references Categoria (IDCategoria);

alter table Recensione add constraint FKCorrisponde_FK
     foreign key (Codice_Ordine)
     references Ordine (Codice_Ordine);

alter table Riga_prodotto add constraint EXTONE_Riga_prodotto
     check((RigaProdottoSingolo is not null and RigaProdottoMenu is null)
           or (RigaProdottoSingolo is null and RigaProdottoMenu is not null));

alter table Riga_prodotto add constraint FKContiene
     foreign key (Codice_Ordine)
     references Ordine (Codice_Ordine);

-- Not implemented
-- alter table RigaProdottoMenu add constraint FKRig_Rig_1_CHK
--     check(exists(select * from ComponenteMenuOrdinato
--                  where ComponenteMenuOrdinato.Codice_Ordine = Codice_Ordine and ComponenteMenuOrdinato.CodiceRiga = CodiceRiga));

alter table RigaProdottoMenu add constraint FKRig_Rig_1_FK
     foreign key (Codice_Ordine, CodiceRiga)
     references Riga_prodotto (Codice_Ordine, CodiceRiga);

alter table RigaProdottoMenu add constraint FKRiferisceMenu_FK
     foreign key (Codice_Prodotto)
     references Menu (Codice_Prodotto);

alter table RigaProdottoSingolo add constraint FKRig_Rig_FK
     foreign key (Codice_Ordine, CodiceRiga)
     references Riga_prodotto (Codice_Ordine, CodiceRiga);

alter table RigaProdottoSingolo add constraint FKReferencSing_FK
     foreign key (Codice_Prodotto)
     references Singolo (Codice_Prodotto);

-- Not implemented
-- alter table Singolo add constraint FKPro_Sin_CHK
--     check(exists(select * from Comprende
--                  where Comprende.Codice_Prodotto = Codice_Prodotto));

alter table Singolo add constraint FKPro_Sin_FK
     foreign key (Codice_Prodotto)
     references Prodotto (Codice_Prodotto);

alter table Stato_Ordine add constraint FKAggiorna_Stato
     foreign key (Codice_Ordine)
     references Ordine (Codice_Ordine);


-- Index Section
-- _____________

create unique index ID_Categoria_IND
     on Categoria (IDCategoria);

create unique index ID_Cliente_IND
     on Cliente (Codice_Utente);

create unique index SID_Cliente_IND
     on Cliente (Email);

create unique index ID_ComponenteMenuOrdinato_IND
     on ComponenteMenuOrdinato (Codice_Ordine, CodiceRiga, NumRowCompMenu);

create index FKRiferisceCompMenu_IND
     on ComponenteMenuOrdinato (Codice_Prodotto);

create unique index ID_CompostoMenu_IND
     on CompostoMenu (Codice_Menu, Codice_Prodotto);

create index FKCom_Sin_1_IND
     on CompostoMenu (Codice_Prodotto);

create unique index ID_Comprende_IND
     on Comprende (Codice_Ingrediente, Codice_Prodotto);

create index FKCom_Sin_IND
     on Comprende (Codice_Prodotto);

create unique index ID_Ingrediente_IND
     on Ingrediente (Codice_Ingrediente);

create unique index FKPro_Men_IND
     on Menu (Codice_Prodotto);

create unique index ID_ModificaComponenteMenu_IND
     on ModificaComponenteMenu (Codice_Ordine, CodiceRiga, NumRowCompMenu, Codice_Ingrediente);

create index FKMod_Ing_1_IND
     on ModificaComponenteMenu (Codice_Ingrediente);

create unique index ID_ModificaProdottoSingolo_IND
     on ModificaProdottoSingolo (Codice_Ingrediente, Codice_Ordine, CodiceRiga);

create index FKMod_Rig_IND
     on ModificaProdottoSingolo (Codice_Ordine, CodiceRiga);

create unique index ID_Ordine_IND
     on Ordine (Codice_Ordine);

create index FKPrende_in_carico_IND
     on Ordine (Codice_Rider);

create index FKCrea_IND
     on Ordine (Codice_Utente);

create unique index ID_Prodotto_IND
     on Prodotto (Codice_Prodotto);

create index FKAppartiene_IND
     on Prodotto (IDCategoria);

create unique index FKCorrisponde_IND
     on Recensione (Codice_Ordine);

create unique index ID_Rider_IND
     on Rider (Codice_Rider);

create unique index SID_Rider_IND
     on Rider (Email);

create unique index ID_Riga_prodotto_IND
     on Riga_prodotto (Codice_Ordine, CodiceRiga);

create unique index FKRig_Rig_1_IND
     on RigaProdottoMenu (Codice_Ordine, CodiceRiga);

create index FKRiferisceMenu_IND
     on RigaProdottoMenu (Codice_Prodotto);

create unique index FKRig_Rig_IND
     on RigaProdottoSingolo (Codice_Ordine, CodiceRiga);

create index FKReferencSing_IND
     on RigaProdottoSingolo (Codice_Prodotto);

create unique index FKPro_Sin_IND
     on Singolo (Codice_Prodotto);

create unique index ID_Stato_Ordine_IND
     on Stato_Ordine (Codice_Ordine, Tempo, Stato);


-- *********************************************
-- * Script di popolamento
-- * Database: Schema_Rel_FastFood
-- * Da eseguire DOPO aver creato lo schema con
-- * lo script di generazione DB-MAIN
-- *********************************************

use Schema_Rel_FastFood;

-- -------------------------------------------------
-- 1) Tabelle "radice" (nessuna dipendenza da altre)
-- -------------------------------------------------

-- Categoria
insert into Categoria (IDCategoria, Nome) values
('CAT01', 'Panini'),
('CAT02', 'Bevande'),
('CAT03', 'Contorni'),
('CAT05', 'Dolci');

-- Ingrediente
insert into Ingrediente (Vegano, Glutine, Lattosio, Nome_Ingrediente, Codice_Ingrediente) values
(false, true,  false, 'Pane Bun',            'ING01'),
(false, false, false, 'Hamburger di Manzo',  'ING02'),
(false, false, true,  'Formaggio Cheddar',   'ING03'),
(true,  false, false, 'Lattuga',             'ING04'),
(true,  false, false, 'Pomodoro',            'ING05'),
(true,  false, false, 'Patate',              'ING06'),
(true,  false, false, 'Sale',                'ING07'),
(true,  false, false, 'Cola',                'ING08'),
(false, false, false, 'Bacon',               'ING09'),
(true,  false, false, 'Salsa Ketchup',       'ING10');

-- Cliente
insert into Cliente (Telefono, Codice_Utente, Username, Password, Email, Nome, Cognome, Data_di_Nascita) values
('3331234567', 'CLI001', 'mrossi',   'pass123', 'mario.rossi@email.com',   'Mario',  'Rossi',   '1990-05-12'),
('3339876543', 'CLI002', 'lbianchi', 'pass456', 'laura.bianchi@email.com', 'Laura',  'Bianchi', '1988-11-23'),
('3345551122', 'CLI003', 'gverdi',   'pass789', 'giulia.verdi@email.com',  'Giulia', 'Verdi',   '1995-02-08'),
('3356667788', 'CLI004', 'pferrari', 'passabc', 'paolo.ferrari@email.com', 'Paolo',  'Ferrari', '1992-07-30'),
('3367778899', 'CLI005', 'sgalli',   'passdef', 'sara.galli@email.com',    'Sara',   'Galli',   '1998-09-15');

-- Rider
insert into Rider (Telefono, Codice_Rider, Username, Password, Email, Nome, Cognome, Data_di_Nascita, RaitingMedioRider, Guadagno) values
('3391112233', 'RID001', 'mrusso',  'riderpass1', 'marco.russo@email.com',  'Marco', 'Russo',  '1994-03-21', 4.8, 1500.50),
('3392223344', 'RID002', 'aromano', 'riderpass2', 'anna.romano@email.com',  'Anna',  'Romano', '1996-06-17', 4.5, 1200.00),
('3393334455', 'RID003', 'lmarino', 'riderpass3', 'luca.marino@email.com',  'Luca',  'Marino', '1993-12-05', 4.9, 1800.75);

-- -------------------------------------------------
-- 2) Prodotto (dipende da Categoria)
-- -------------------------------------------------
-- Nota: Singolo/Menu sono flag ('S'/'M') per soddisfare il CHECK di esclusivita'
insert into Prodotto (Disponibile, Codice_Prodotto, Nome_Prodotto, Prezzo_originario, Descrizione_Prodotto, Singolo, Menu, IDCategoria) values
('S', 'PRD01', 'Cheeseburger',        5.00,  'Panino con hamburger di manzo e formaggio cheddar', 'S', NULL, 'CAT01'),
('S', 'PRD02', 'Hamburger',           4.00,  'Panino con hamburger di manzo, lattuga e pomodoro',  'S', NULL, 'CAT01'),
('S', 'PRD03', 'Patatine Fritte',     2.50,  'Porzione di patatine fritte croccanti',              'S', NULL, 'CAT03'),
('S', 'PRD04', 'Coca Cola',           2.00,  'Bevanda gassata 33cl',                                'S', NULL, 'CAT02'),
('S', 'PRD05', 'Bacon Burger',        5.50,  'Panino con hamburger di manzo, bacon e cheddar',     'S', NULL, 'CAT01'),
('S', 'PRD06', 'Insalata',            4.00,  'Insalata fresca con lattuga e pomodoro',              'S', NULL, 'CAT03'),
('S', 'PRD07', 'Gelato',              2.50,  'Coppa di gelato artigianale',                          'S', NULL, 'CAT05'),
('S', 'PRD08', 'Menu Cheeseburger',   8.50,  'Cheeseburger + patatine + bevanda',                   NULL, 'M', 'CAT01'),
('S', 'PRD09', 'Menu Bacon Burger',   9.00,  'Bacon Burger + patatine + bevanda',                    NULL, 'M', 'CAT01'),
('N', 'PRD10', 'Menu Family',         16.00, 'Doppio menu Cheeseburger + Bacon Burger da condividere', NULL, 'M', 'CAT01');

-- -------------------------------------------------
-- 3) Sottotipi di Prodotto: Singolo / Menu
-- -------------------------------------------------

insert into Singolo (Codice_Prodotto) values
('PRD01'), ('PRD02'), ('PRD03'), ('PRD04'), ('PRD05'), ('PRD06'), ('PRD07');

insert into Menu (Codice_Prodotto) values
('PRD08'), ('PRD09'), ('PRD10');

-- -------------------------------------------------
-- 4) Composizione dei prodotti (dipende da Ingrediente e Singolo)
-- -------------------------------------------------

insert into Comprende (Codice_Ingrediente, Codice_Prodotto, Quantita) values
('ING01', 'PRD01', 1),  -- Cheeseburger: pane
('ING02', 'PRD01', 1),  -- Cheeseburger: manzo
('ING03', 'PRD01', 1),  -- Cheeseburger: cheddar
('ING04', 'PRD01', 1),  -- Cheeseburger: lattuga
('ING05', 'PRD01', 1),  -- Cheeseburger: pomodoro
('ING01', 'PRD02', 1),  -- Hamburger: pane
('ING02', 'PRD02', 1),  -- Hamburger: manzo
('ING04', 'PRD02', 1),  -- Hamburger: lattuga
('ING05', 'PRD02', 1),  -- Hamburger: pomodoro
('ING06', 'PRD03', 1),  -- Patatine: patate
('ING07', 'PRD03', 1),  -- Patatine: sale
('ING08', 'PRD04', 1),  -- Coca Cola: cola
('ING01', 'PRD05', 1),  -- Bacon Burger: pane
('ING02', 'PRD05', 1),  -- Bacon Burger: manzo
('ING03', 'PRD05', 1),  -- Bacon Burger: cheddar
('ING09', 'PRD05', 1),  -- Bacon Burger: bacon
('ING04', 'PRD06', 1),  -- Insalata: lattuga
('ING05', 'PRD06', 1);  -- Insalata: pomodoro

-- -------------------------------------------------
-- 5) Composizione dei Menu (dipende da Menu e Singolo)
-- -------------------------------------------------

insert into CompostoMenu (Codice_Menu, Codice_Prodotto, quantita) values
('PRD08', 'PRD01', 1),  -- Menu Cheeseburger: Cheeseburger
('PRD08', 'PRD03', 1),  -- Menu Cheeseburger: Patatine
('PRD08', 'PRD04', 1),  -- Menu Cheeseburger: Coca Cola
('PRD09', 'PRD05', 1),  -- Menu Bacon Burger: Bacon Burger
('PRD09', 'PRD03', 1),  -- Menu Bacon Burger: Patatine
('PRD09', 'PRD04', 1),  -- Menu Bacon Burger: Coca Cola
('PRD10', 'PRD01', 2),  -- Menu Family: 2x Cheeseburger
('PRD10', 'PRD05', 2),  -- Menu Family: 2x Bacon Burger
('PRD10', 'PRD03', 2),  -- Menu Family: 2x Patatine
('PRD10', 'PRD04', 2);  -- Menu Family: 2x Coca Cola

-- -------------------------------------------------
-- 6) Ordine (dipende da Cliente e Rider)
-- -------------------------------------------------

insert into Ordine (DataCreazione, Ind_Via, Ind_Citta, Ind_Civico, Codice_Ordine, Codice_Rider, Codice_Utente) values
('2026-08-15', 'Via Roma',       'Bari', '10', 'ORD001', 'RID001', 'CLI001'),
('2026-08-16', 'Via Dante',      'Bari', '5A', 'ORD002', 'RID002', 'CLI002'),
('2026-08-17', 'Corso Cavour',   'Bari', '22', 'ORD003', NULL,     'CLI003'),
('2026-08-18', 'Via Sparano',    'Bari', '8',  'ORD004', 'RID003', 'CLI004'),
('2026-08-18', 'Via Argiro',     'Bari', '15', 'ORD005', 'RID001', 'CLI005');

-- -------------------------------------------------
-- 7) Riga_prodotto (dipende da Ordine)
-- -------------------------------------------------
-- RigaProdottoMenu/RigaProdottoSingolo sono flag ('M'/'S') per il CHECK di esclusivita'

insert into Riga_prodotto (Codice_Ordine, CodiceRiga, Quantita, Prezzo, RigaProdottoMenu, RigaProdottoSingolo) values
('ORD001', 'R00011', 1.00, 8.50,  'M', NULL),
('ORD001', 'R00012', 1.00, 2.50,  NULL, 'S'),
('ORD002', 'R00021', 1.00, 9.00,  'M', NULL),
('ORD003', 'R00031', 2.00, 10.00, NULL, 'S'),
('ORD003', 'R00032', 2.00, 4.00,  NULL, 'S'),
('ORD004', 'R00041', 1.00, 16.00, 'M', NULL),
('ORD005', 'R00051', 1.00, 4.00,  NULL, 'S'),
('ORD005', 'R00052', 1.00, 2.50,  NULL, 'S');

-- -------------------------------------------------
-- 8) RigaProdottoMenu / RigaProdottoSingolo
-- -------------------------------------------------

insert into RigaProdottoMenu (Codice_Ordine, CodiceRiga, Codice_Prodotto) values
('ORD001', 'R00011', 'PRD08'),  -- Menu Cheeseburger
('ORD002', 'R00021', 'PRD09'),  -- Menu Bacon Burger
('ORD004', 'R00041', 'PRD10'); -- Menu Family

insert into RigaProdottoSingolo (Codice_Ordine, CodiceRiga, Codice_Prodotto) values
('ORD001', 'R00012', 'PRD07'),  -- Gelato
('ORD003', 'R00031', 'PRD01'),  -- Cheeseburger
('ORD003', 'R00032', 'PRD04'),  -- Coca Cola
('ORD005', 'R00051', 'PRD06'),  -- Insalata
('ORD005', 'R00052', 'PRD03'); -- Patatine

-- -------------------------------------------------
-- 9) ComponenteMenuOrdinato (dipende da RigaProdottoMenu e Singolo)
-- -------------------------------------------------

insert into ComponenteMenuOrdinato (Codice_Ordine, CodiceRiga, NumRowCompMenu, Codice_Prodotto) values
-- Menu Cheeseburger ordinato in ORD001/R00011
('ORD001', 'R00011', 1, 'PRD01'),
('ORD001', 'R00011', 2, 'PRD03'),
('ORD001', 'R00011', 3, 'PRD04'),
-- Menu Bacon Burger ordinato in ORD002/R00021
('ORD002', 'R00021', 1, 'PRD05'),
('ORD002', 'R00021', 2, 'PRD03'),
('ORD002', 'R00021', 3, 'PRD04'),
-- Menu Family ordinato in ORD004/R00041
('ORD004', 'R00041', 1, 'PRD01'),
('ORD004', 'R00041', 2, 'PRD05'),
('ORD004', 'R00041', 3, 'PRD03'),
('ORD004', 'R00041', 4, 'PRD04');

-- -------------------------------------------------
-- 10) Modifiche a componenti di menu e a prodotti singoli
-- -------------------------------------------------

insert into ModificaComponenteMenu (Codice_Ordine, CodiceRiga, NumRowCompMenu, Codice_Ingrediente, Quantita, Tipo) values
('ORD001', 'R00011', 1, 'ING05', 0, 'Rimozione'),  -- niente pomodoro nel cheeseburger del menu
('ORD002', 'R00021', 1, 'ING09', 2, 'Aggiunta');   -- doppio bacon nel bacon burger del menu

insert into ModificaProdottoSingolo (Codice_Ordine, CodiceRiga, Codice_Ingrediente, Quantita, Tipo) values
('ORD003', 'R00031', 'ING03', 2, 'Aggiunta'),   -- doppio formaggio sul cheeseburger singolo
('ORD005', 'R00051', 'ING05', 0, 'Rimozione');  -- insalata senza pomodoro

-- -------------------------------------------------
-- 11) Stato_Ordine (dipende da Ordine)
-- -------------------------------------------------

insert into Stato_Ordine (Codice_Ordine, Tempo, Stato) values
('ORD001', '12:10:00', 'Pronto'),
('ORD001', '12:00:00', 'In preparazione'),
('ORD001', '12:30:00', 'In Consegna'),
('ORD001', '12:50:00', 'Consegnato'),

('ORD002', '19:15:00', 'Pronto'),
('ORD002', '19:00:00', 'In preparazione'),
('ORD002', '19:40:00', 'In Consegna'),
('ORD002', '20:00:00', 'Consegnato'),

('ORD003', '13:15:00', 'Pronto'),
('ORD003', '13:00:00', 'In preparazione'),

('ORD004', '20:20:00', 'Pronto'),
('ORD004', '20:00:00', 'In preparazione'),
('ORD004', '20:45:00', 'In consegna'),
('ORD004', '21:10:00', 'Consegnato'),

('ORD005', '21:10:00', 'Pronto'),
('ORD005', '21:00:00', 'In preparazione'),
('ORD005', '21:35:00', 'In consegna');

-- -------------------------------------------------
-- 12) Recensione (solo per ordini consegnati; dipende da Ordine)
-- -------------------------------------------------

insert into Recensione (Codice_Ordine, Voto_Rider, Testo_Recensione, Voto_Ordine) values
('ORD001', 5.0, 'Consegna veloce e cibo caldo!',   4.80),
('ORD002', 4.5, 'Tutto ottimo, rider gentile.',     4.50),
('ORD004', 5.0, 'Perfetto in ogni dettaglio.',      5.00);