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


    // =================================================
    // SCONTO (+ sottotipi)
    // =================================================

    INSERIRE_SCONTO("""
            INSERT INTO Sconto (Codice_Sconto, Data_Inizio, Data_Fine, Percentuale_Scontata, Descrizione, ScontoOffertoDallaCasa)
            VALUES(?,?,?,?,?,?)
            """),

    INSERIRE_SCONTO_FEDELTA_PADRE("""
            INSERT INTO Sconto (Codice_Sconto, Data_Inizio, Data_Fine, Percentuale_Scontata, Descrizione, ScontoFedelta)
            VALUES(?,?,?,?,?,?)
            """),

    INSERIRE_SCONTOCASA("""
            INSERT INTO ScontoOffertoDallaCasa (Codice_Sconto) VALUES (?)
            """),

    INSERIRE_SCONTOFEDEL("""
            INSERT INTO ScontoFedelta (Codice_Sconto, QuotaOrdini, NumeroUtilizzi)
            VALUES(?, ?, ?)
            """),

    MOSTRA_SCONTI("""
            SELECT s.*,
                   CASE WHEN s.ScontoFedelta IS NOT NULL THEN 'Fedelta'
                        WHEN s.ScontoOffertoDallaCasa IS NOT NULL THEN 'Casa'
                        ELSE 'Sconosciuto' END AS Tipo
            FROM Sconto s
            """),


    // =================================================
    // CLIENTE / RIDER
    // =================================================

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

    MOSTRA_CLIENTI("""
            SELECT * FROM Cliente
            """),

    CERCA_CLIENTE_PER_Email("""
            SELECT * FROM Cliente WHERE Email = ? AND Password = ?
            """),


    AGGIORNA_NUM_ORDINI_CLIENTE("""
            UPDATE Cliente SET NumOrdiniEffetuati = NumOrdiniEffetuati + 1
            WHERE Codice_Utente = ?
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
            UPDATE Rider
            SET RaitingMedioRider = (
                SELECT AVG(r.Voto_Rider)
                FROM Recensione r
                JOIN Prende_in_carico p ON p.Codice_Ordine = r.Codice_Ordine
                WHERE p.Codice_Rider = ?
            )
            WHERE Codice_Rider = ?
            """),

    MOSTRA_ULTIMO_RIDER_CODICE("""
            SELECT Codice_Rider FROM Rider ORDER BY Codice_Rider DESC LIMIT 1
            """),

    AGGIORNA_ORDINI_CONSEGNATI_RIDER("""
            UPDATE Rider SET Ordini_Totali_Consegnati = Ordini_Totali_Consegnati + 1
            WHERE Codice_Rider = ?
            """),

    AGGIORNA_GUADAGNO_TOTALE_RIDER("""
            UPDATE Rider
            SET GuadagnoTotale = GuadagnoTotale + ?
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



    // =================================================
    // PRODOTTO (+ Singolo/Menu)
    // =================================================

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
            DELETE FROM CompostoMenu WHERE Codice_Prodotto = ?
            """),

    ELIMINA_RICETTA("""
            DELETE FROM Comprende WHERE Codice_Prodotto = ?
            """),


    // =================================================
    // ORDINE
    // =================================================

    INSERIRE_ORDINE("""
            INSERT INTO Ordine (Prezzo_totale, DataCreazione, Ind_Via, Ind_Citta, Ind_Civico, Codice_Ordine, Codice_Utente)
            VALUES (?,?,?,?,?,?,?,?,?,?)
            """),

    MOSTRA_ORDINI("""
            SELECT * FROM Ordine
            """),

    MOSTRA_ORDINI_PRONTI("""
                SELECT *
                FROM stato_ordine
                INNER JOIN ordine ON ordine.Codice_Ordine = stato_ordine.Codice_Ordine
                WHERE stato_ordine.Stato = 'Pronto' AND ordine.Codice_Rider IS NULL;
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

    MOSTRA_ORDINI_ASSEGNATI_RIDER("""
            SELECT o.* FROM Ordine o
            JOIN Prende_in_carico p ON p.Codice_Ordine = o.Codice_Ordine
            WHERE p.Codice_Utente = ?
            ORDER BY o.DataCreazione DESC
            """),

    // =================================================
    // PRENDE_IN_CARICO
    // =================================================

    INSERIRE_PRENDE_IN_CARICO("""
            INSERT INTO Prende_in_carico (Codice_Ordine, Compenso_Rider, Codice_Utente)
            VALUES (?, ?, ?)
            """),

    TROVA_RIDER_DELLA_CONSEGNA("""
            SELECT Codice_Utente FROM Prende_in_carico WHERE Codice_Ordine = ?
            """),


    // =================================================
    // RIGA_PRODOTTO (+ sottotipi)
    // =================================================

    INSERIRE_RIGA_PRODOTTO_SINGOLA("""
            INSERT INTO Riga_prodotto (Codice_Ordine, CodiceRiga, Quantita, Prezzo, Codice_Sconto, RigaProdottoSingolo)
            VALUES (?, ?, ?, ?, ?, ?)
            """),

    INSERIRE_RIGA_PRODOTTO_MENU("""
            INSERT INTO Riga_prodotto (Codice_Ordine, CodiceRiga, Quantita, Prezzo, Codice_Sconto, RigaProdottoMenu)
            VALUES (?, ?, ?, ?, ?, ?)
            """),

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
            INSERT INTO ComponenteMenuOrdinato (Codice_Ordine, CodiceRiga, NumRowCompMenu)
            VALUES (?, ?, ?)
            """),

    INSERIRE_RIFERISCE_COMP_MENU("""
            INSERT INTO RiferisceCompMenu (Codice_Ordine, CodiceRiga, NumRowCompMenu, Codice_Prodotto)
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

    INSERIRE_MODIFICA_PRODOTTO_SINGOLO("""
            INSERT INTO ModificaProdottoSingolo (Codice_Ordine, CodiceRiga, Codice_Ingrediente, Tipo)
            VALUES (?, ?, ?, ?)
            """),



    INSERIRE_MODIFICA_COMPONENTE_MENU("""
            INSERT INTO ModificaComponenteMenu (Codice_Ordine, CodiceRiga, NumRowCompMenu, Codice_Ingrediente, Tipo)
            VALUES (?, ?, ?, ?, ?)
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
        """);


    private final String query;

    Queries(final String query) {
        this.query = query;
    }

    public String get() {
        return this.query;
    }
}