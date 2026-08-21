package project.db;

public enum Queries {

        // =================================================
        // CATEGORIA / INGREDIENTE
        // =================================================

        INSERIRE_CATEGORIA("""
                        INSERT INTO Categoria (IDCategoria, Nome)
                        VALUES(?,?)
                        """),

        MOSTRA_CATEGORIE("""
                        SELECT * FROM Categoria
                        """),

        INSERIRE_INGREDIENTE("""
                        INSERT INTO Ingrediente (Codice_Ingrediente, Nome_Ingrediente, Vegano, Glutine, Lattosio)
                        VALUES(?,?,?,?,?)
                        """),

        MOSTRA_INGREDIENTI("""
                        SELECT * FROM Ingrediente
                        """),

        GET_LAST_INGREDIENTE("""
                        SELECT Codice_Ingrediente FROM Ingrediente ORDER BY Codice_Ingrediente DESC LIMIT 1
                        """),
        GET_LAST_CATEGORIA("""
                        SELECT IDCategoria FROM Categoria ORDER BY IDCategoria DESC LIMIT 1
                        """),

        GET_CATEGORIA_BY_NAME("""
                        SELECT IDCategoria FROM Categoria WHERE Nome = ?
                        """),

        TROVA_INGREDIENTE("""
                        SELECT * FROM Ingrediente
                        WHERE Nome_Ingrediente = ?
                        """),

        INGREDIENTI_NON_PRESENTI("""
                        SELECT i.Nome_Ingrediente
                        FROM Ingrediente i
                        WHERE NOT EXISTS (
                        SELECT 1
                        FROM Comprende c
                        WHERE c.Codice_Prodotto = ?
                        AND c.Codice_Ingrediente = i.Codice_Ingrediente
                        );
                        """),

        INGREDIENTI_CONTENUTI("""
                        SELECT nome_ingrediente
                        FROM Comprende c
                        INNER JOIN Ingrediente i ON i.Codice_Ingrediente = c.Codice_Ingrediente
                        where Codice_Prodotto = ?
                        """),
        INGREDIENTI_CONTENUTI_Quantita("""
                        SELECT nome_ingrediente, quantita
                        FROM Comprende c
                        INNER JOIN Ingrediente i ON i.Codice_Ingrediente = c.Codice_Ingrediente
                        where Codice_Prodotto = ?
                        """),
        GET_INGREDIENTE_BY_NAME("""
                        SELECT Codice_Ingrediente FROM Ingrediente WHERE Nome_Ingrediente = ?
                        """),

        // CLIENTE / RIDER

        INSERIRE_CLIENTE("""
                        INSERT INTO Cliente (Codice_Utente, Username, Password, Email, Nome,
                        Cognome, Data_di_Nascita, Telefono)
                        VALUES(?,?,?,?,?,?,?,?)
                        """),
        MOSTRA_ULTIMO_CLIENTE_CODICE("""
                        SELECT Codice_Utente FROM Cliente ORDER BY Codice_Utente DESC LIMIT 1
                        """),

        CERCA_RIDER("""
                        select *
                        from rider
                        where rider.Email = ? and rider.Password = ?
                        """),

        CERCA_CLIENT_EMAIL("""
                        select *
                        from cliente
                        where cliente.Email = ?
                        """),

        CERCA_RIDER_EMAIL("""
                        select *
                        from rider
                        where rider.Email = ?
                        """),

        MOSTRA_CLIENTI("""
                        SELECT * FROM Cliente
                        """),

        CERCA_CLIENTE_PER_Email("""
                        SELECT * FROM Cliente WHERE Email = ? AND Password = ?
                        """),

        INSERIRE_RIDER("""
                        INSERT INTO Rider (Codice_Rider, Username, Password, Email, Nome,
                        Cognome, Data_di_Nascita, Telefono, RaitingMedioRider, Guadagno)
                        VALUES(?,?,?,?,?,?,?,?,?,?)
                        """),

        MOSTRA_RIDER("""
                        SELECT * FROM Rider
                        """),

        AGGIORNA_MEDIA_RIDER("""
                        update rider
                        SET RaitingMedioRider = (
                        select avg(Voto_Rider)
                        from recensione as r
                        inner join ordine as o on r.Codice_Ordine = o.Codice_Ordine
                        where Codice_Rider = ?
                        )
                        where Codice_rider = ?
                        """),

        MOSTRA_ULTIMO_RIDER_CODICE("""
                        SELECT Codice_Rider FROM Rider ORDER BY Codice_Rider DESC LIMIT 1
                        """),

        AGGIORNA_GUADAGNO_TOTALE_RIDER("""
                        UPDATE Rider
                        SET Guadagno = Guadagno + ?
                        WHERE Codice_Rider = ?;
                        """),

        AGGIORNA_ORDINE_RIDER("""
                        UPDATE Ordine
                        SET Codice_Rider = ?
                        WHERE Codice_Ordine = ?
                        """),

        GET_LAST_RIDER_CODICE("""
                        SELECT Codice_Rider FROM Rider ORDER BY Codice_Rider DESC LIMIT 1
                        """),

        INSERIRE_DENTRO_ORDINE_RIDER("""
                        UPDATE Ordine
                        SET Codice_Rider = ?
                        WHERE Codice_Ordine = ?
                        """),

        CLASSIFICA_MIGLIORI_RIDER("""
                        select Nome, Cognome, RaitingMedioRider, Guadagno
                        from rider
                        order by  RaitingMedioRider desc;
                        """),

        // =================================================
        // PRODOTTO / MENU / SINGOLO
        // =================================================

        PRODOTTI_MIGLIORI_CLASSIFICA("""
                        SELECT p.Codice_Prodotto,
                        p.Nome_Prodotto,
                        COALESCE(vs.Tot_Singolo, 0) + COALESCE(vm.Tot_Menu, 0) AS Totale_Venduto
                        FROM Prodotto p
                        LEFT JOIN (
                        SELECT Codice_Prodotto, COUNT(*) AS Tot_Singolo
                        FROM rigaprodottosingolo
                        GROUP BY Codice_Prodotto
                        ) vs ON p.Codice_Prodotto = vs.Codice_Prodotto
                        LEFT JOIN (
                        SELECT Codice_Prodotto, COUNT(*) AS Tot_Menu
                        FROM rigaprodottomenu
                        GROUP BY Codice_Prodotto
                        ) vm ON p.Codice_Prodotto = vm.Codice_Prodotto
                        ORDER BY Totale_Venduto DESC
                        LIMIT 5;

                        """),

        MOSTRA_PRODOTTI_MENU_QUANTITA("""
                        SELECT p.Nome_Prodotto, cm.quantita
                        FROM CompostoMenu cm
                        JOIN Prodotto p ON p.Codice_Prodotto = cm.Codice_Prodotto
                        WHERE cm.Codice_Menu = ?
                        """),

        TROVA_MENU_CHE_CONTENGONO_PRODOTTO("""
                        SELECT m.Codice_Prodotto, p.Nome_Prodotto
                        FROM CompostoMenu cm
                        JOIN Menu m ON m.Codice_Prodotto = cm.Codice_Menu
                        JOIN Prodotto p ON p.Codice_Prodotto = m.Codice_Prodotto
                        WHERE cm.Codice_Prodotto = ?
                        """),

        RENDI_NON_DISPONIBILE("""
                        UPDATE Prodotto SET Disponibile = 'N' WHERE Codice_Prodotto = ?
                        """),

        GET_LAST_PRODOTTO("""
                        SELECT Codice_Prodotto FROM Prodotto ORDER BY Codice_Prodotto DESC LIMIT 1
                        """),

        GET_LAST_SINGOLO("""
                        SELECT Codice_Prodotto FROM Singolo ORDER BY Codice_Prodotto DESC LIMIT 1
                        """),

        ELIMINA_COMPONENTI_MENU_CATALOGO("""
                        DELETE FROM CompostoMenu WHERE Codice_Prodotto = ?
                        """),

        ELIMINA_MENU("""
                        DELETE FROM Menu WHERE Codice_Prodotto = ?
                        """),

        INSERIRE_PRODOTTO("""
                        INSERT INTO Prodotto (Codice_Prodotto, Nome_Prodotto, Descrizione_Prodotto, Prezzo_originario,
                        Disponibile, IDCategoria, Singolo, Menu)
                        VALUES(?,?,?,?,?,?,?,?)
                        """),

        INSERIRE_MENU_PADRE("""
                        INSERT INTO Prodotto (Codice_Prodotto, Nome_Prodotto, Descrizione_Prodotto, Prezzo_originario,
                        Disponibile, IDCategoria, Menu)
                        VALUES(?,?,?,?,?,?,?)
                        """),

        INSERIRE_SINGOLO("""
                        INSERT INTO Singolo (Codice_Prodotto) VALUES (?)
                        """),

        INSERIRE_MENU("""
                        INSERT INTO Menu (Codice_Prodotto) VALUES (?)
                        """),

        MOSTRA_PRODOTTI("""
                        SELECT p.*,
                                CASE WHEN s.Codice_Prodotto IS NOT NULL THEN 'Singolo'
                                WHEN m.Codice_Prodotto IS NOT NULL THEN 'Menu'
                                ELSE 'Sconosciuto' END AS Tipo
                        FROM Prodotto p
                        LEFT JOIN Singolo s ON s.Codice_Prodotto = p.Codice_Prodotto
                        LEFT JOIN Menu m ON m.Codice_Prodotto = p.Codice_Prodotto
                        """),

        CERCA_PRODOTTO_PER_CODICE("""
                        SELECT * FROM Prodotto WHERE Codice_Prodotto = ?
                        """),

        AGGIORNA_PRODOTTO("""
                        UPDATE Prodotto
                        SET Nome_Prodotto = ?, Descrizione_Prodotto = ?, Prezzo_originario = ?,
                        Disponibile = ?
                        WHERE Codice_Prodotto = ?
                        """),

        TROVA_MENU_CHE_CONTENGONO("""
                        SELECT m.Codice_Prodotto, p.Nome_Prodotto
                        FROM CompostoMenu cm
                        JOIN Menu m ON m.Codice_Prodotto = cm.Codice_Menu
                        JOIN Prodotto p ON p.Codice_Prodotto = m.Codice_Prodotto
                        WHERE cm.Codice_Prodotto = ?
                        """),

        VERIFICA_PRODOTTO_ORDINATO("""
                        SELECT 1 FROM RigaProdottoSingolo WHERE Codice_Prodotto = ? LIMIT 1
                        """),

        ELIMINA_PRODOTTO_SINGOLO("""
                        DELETE FROM Singolo WHERE Codice_Prodotto = ?
                        """),

        ELIMINA_PRODOTTO("""
                        DELETE FROM Prodotto WHERE Codice_Prodotto = ?
                        """),
        GET_CODICE_PRODOTTO_BY_NAME("""
                        SELECT Codice_Prodotto FROM Prodotto WHERE Nome_Prodotto = ?
                        """),
        IS_PRODOTTO_MENU("""
                        SELECT 1 FROM Menu WHERE Codice_Prodotto = ?
                        """),

        // =================================================
        // COMPRENDE / COMPOSTO_MENU
        // =================================================

        INSERIRE_COMPRENDE("""
                        INSERT INTO Comprende (Codice_Ingrediente, Codice_Prodotto, Quantita) VALUES (?, ?, ?)
                        """),

        MOSTRA_RICETTA_PRODOTTO("""
                        SELECT i.Nome_Ingrediente, c.quantita
                        FROM Comprende c
                        JOIN Ingrediente i ON i.Codice_Ingrediente = c.Codice_Ingrediente
                        WHERE c.Codice_Prodotto = ?
                        """),

        MOSTRA_SINGOLO_INGREDIENTE("""
                        SELECT i.Nome_Ingrediente, c.quantita
                        FROM  Ingrediente i
                        JOIN Comprende c ON i.Codice_Ingrediente = c.Codice_Ingrediente
                        WHERE c.Codice_Prodotto = 'M01' and c.Codice_Ingrediente = 'I11';
                        """),

        INSERIRE_COMPOSTO_MENU("""
                        INSERT INTO CompostoMenu (Codice_Menu, Codice_Prodotto, quantita) VALUES (?, ?, ?)
                        """),

        MOSTRA_COMPONENTI_MENU_CATALOGO("""
                        SELECT p.*, cm.quantita
                        FROM CompostoMenu cm
                        JOIN Prodotto p ON p.Codice_Prodotto = cm.Codice_Prodotto
                        WHERE cm.Codice_Menu = ?
                        """),

        ELIMINA_DA_COMPOSTO_MENU("""
                        DELETE FROM CompostoMenu WHERE Codice_Menu = ?
                        """),

        ELIMINA_RICETTA("""
                        DELETE FROM Comprende WHERE Codice_Prodotto = ?
                        """),

        MOSTRA_QUANTITA_INGREDIENTE_PRODOTTO("""
                        SELECT quantita
                        FROM comprende
                        WHERE Codice_Prodotto = ? AND Codice_Ingrediente = ?;
                        """),

        // =================================================
        // ORDINE E STATO_ORDINE
        // =================================================

        INSERIRE_STATO_ORDINE("""
                        INSERT INTO Stato_Ordine (Codice_Ordine, Stato, Data, Tempo)
                        VALUES (?, ?, ?, ?)
                        """),

        MOSTRA_STATO_ORDINE("""
                        SELECT * FROM Stato_Ordine WHERE Codice_Ordine = ?
                        """),

        INSERIRE_ORDINE("""
                        INSERT INTO Ordine ( DataCreazione, Ind_Via, Ind_Citta, Ind_Civico, Codice_Ordine, Codice_Utente)
                        VALUES (?,?,?,?,?,?)
                        """),

        MOSTRA_ORDINI("""
                        SELECT * FROM Ordine
                        """),

        CERCA_ORDINE_PER_CODICE("""
                        SELECT * FROM Ordine WHERE Codice_Ordine = ?
                        """),

        MOSTRA_ORDINI_CLIENTE("""
                        SELECT * FROM Ordine WHERE Codice_Utente = ?
                        ORDER BY DataCreazione DESC
                        """),

        MOSTRA_ORDINI_DISPONIBILI("""
                        SELECT o.* FROM Ordine o
                        WHERE NOT EXISTS (SELECT 1 FROM Prende_in_carico p WHERE p.Codice_Ordine = o.Codice_Ordine)
                        ORDER BY o.DataCreazione
                        """),

        MOSTRA_ULTIMO_ORDINE_CODICE("""
                        SELECT Codice_Ordine FROM Ordine ORDER BY Codice_Ordine DESC LIMIT 1
                        """),

        MOSTRA_ORDINI_PRONTI("""
                        SELECT *
                        FROM ordine
                        INNER JOIN stato_ordine ON stato_ordine.Codice_Ordine = ordine.Codice_Ordine
                        WHERE stato_ordine.Stato = 'Pronto'
                        AND NOT EXISTS (
                        SELECT 1
                        FROM stato_ordine so2
                        WHERE so2.Codice_Ordine = ordine.Codice_Ordine
                        AND so2.Stato IN ('Consegnato', 'In Consegna')
                        )
                        """),

        MOSTRA_ORDINI_IN_PREPARAZIONE("""
                        SELECT *
                        FROM ordine
                        INNER JOIN stato_ordine ON stato_ordine.Codice_Ordine = ordine.Codice_Ordine
                        WHERE stato_ordine.Stato = 'In Preparazione'
                        AND NOT EXISTS (
                        SELECT 1
                        FROM stato_ordine so2
                        WHERE so2.Codice_Ordine = ordine.Codice_Ordine
                        AND so2.Stato IN ('Consegnato', 'In Consegna', 'Pronto')
                        )
                        """),

        INSERIRE_ORDINE_STATUS("""
                        INSERT INTO Stato_Ordine (Codice_Ordine, Stato, Tempo)
                        VALUES (?, ?, ?)
                        """),
        GET_RIDER_CODE_BY_ORDINE("""
                        SELECT Codice_Rider
                        FROM Ordine
                        WHERE Codice_Ordine = ?
                        """),

        // =================================================
        // RIGA_PRODOTTO (+ sottotipi)
        // =================================================

        INSERIRE_RIGAPRODOTTOSINGOLO("""
                        INSERT INTO RigaProdottoSingolo (Codice_Ordine, CodiceRiga, Codice_Prodotto)
                        VALUES (?, ?, ?)
                        """),

        INSERIRE_RIGAPRODOTTOMENU("""
                        INSERT INTO RigaProdottoMenu (Codice_Ordine, CodiceRiga, Codice_Prodotto)
                        VALUES (?, ?, ?)
                        """),

        MOSTRA_RIGHE_ORDINE("""
                        SELECT * FROM Riga_prodotto WHERE Codice_Ordine = ?
                        ORDER BY CodiceRiga
                        """),

        // =================================================
        // COMPONENTI DI MENU ORDINATI
        // =================================================

        INSERIRE_COMPONENTE_MENU_ORDINATO("""
                        INSERT INTO ComponenteMenuOrdinato (Codice_Ordine, CodiceRiga, NumRowCompMenu, Codice_Prodotto)
                        VALUES (?, ?, ?, ?)
                        """),

        MOSTRA_COMPONENTI_RIGA_MENU("""
                        SELECT c.NumRowCompMenu, r.Codice_Prodotto, p.Nome_Prodotto
                        FROM ComponenteMenuOrdinato c
                        JOIN RiferisceCompMenu r ON r.Codice_Ordine = c.Codice_Ordine
                        AND r.CodiceRiga = c.CodiceRiga AND r.NumRowCompMenu = c.NumRowCompMenu
                        JOIN Prodotto p ON p.Codice_Prodotto = r.Codice_Prodotto
                        WHERE c.Codice_Ordine = ? AND c.CodiceRiga = ?
                        ORDER BY c.NumRowCompMenu
                        """),

        // =================================================
        // PERSONALIZZAZIONI (Modifica*)
        // =================================================

        INSERIRE_MODIFICA_PRODOTTO_SINGOLO(
                        """
                                        INSERT INTO ModificaProdottoSingolo (Codice_Ordine, CodiceRiga, Codice_Ingrediente, Tipo, Quantita)
                                        VALUES (?, ?, ?, ?, ?)
                                        """),

        INSERIRE_MODIFICA_COMPONENTE_MENU(
                        """
                                        INSERT INTO ModificaComponenteMenu (Codice_Ordine, CodiceRiga, NumRowCompMenu, Quantita, Codice_Ingrediente, Tipo)
                                        VALUES (?, ?, ?, ?, ?, ?)
                                        """),

        MOSTRA_MODIFICHE_RIGA_SINGOLA("""
                        SELECT m.Tipo, i.Nome_Ingrediente, m.Quantita
                        FROM ModificaProdottoSingolo m
                        JOIN Ingrediente i ON i.Codice_Ingrediente = m.Codice_Ingrediente
                        WHERE m.Codice_Ordine = ? AND m.CodiceRiga = ?
                        """),

        MOSTRA_MODIFICHE_COMPONENTE("""
                        SELECT m.Tipo, i.Nome_Ingrediente
                        FROM ModificaComponenteMenu m
                        JOIN Ingrediente i ON i.Codice_Ingrediente = m.Codice_Ingrediente
                        WHERE m.Codice_Ordine = ? AND m.CodiceRiga = ? AND m.NumRowCompMenu = ?
                        """),

        // =================================================
        // RECENSIONE
        // =================================================

        MOSTRA_RECENSIONI_NEGATIVE("""
                        select *
                        from recensione
                        where recensione.Voto_Ordine < 3 or recensione.Voto_Rider < 3
                        order by recensione.Voto_Ordine DESC, recensione.Voto_Rider DESC
                        """),

        INSERIRE_RECENSIONE("""
                        INSERT INTO Recensione (Codice_Ordine, Voto_Rider, Testo_Recensione, Voto_Ordine)
                        VALUES (?, ?, ?, ?)
                        """),

        ESISTE_RECENSIONE("""
                        SELECT 1 FROM Recensione WHERE Codice_Ordine = ?
                        """),

        MOSTRA_ORDINI_RECENSIBILI("""
                        SELECT o.*
                        FROM Ordine o
                        INNER JOIN Stato_Ordine so ON so.Codice_Ordine = o.Codice_Ordine
                        WHERE so.Stato = 'Consegnato'
                        AND o.Codice_Utente = ?
                        AND NOT EXISTS (
                        SELECT 1
                        FROM Recensione r
                        WHERE r.Codice_Ordine = o.Codice_Ordine
                        );
                        """),
        MOSTRA_RECENSIONI_NEGATIVE_ORDINE("""
                        SELECT r.Codice_Ordine, r.Testo_Recensione, r.Voto_Ordine, r.Voto_Rider
                        FROM Recensione r
                        WHERE r.Codice_Ordine = ?
                        """);

        private final String query;

        Queries(final String query) {
                this.query = query;
        }

        public String get() {
                return this.query;
        }
}