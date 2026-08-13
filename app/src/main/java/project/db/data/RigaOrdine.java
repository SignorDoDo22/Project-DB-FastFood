package project.db.data;

import project.db.controller.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una riga dell'ordine (tabella Riga_prodotto).
 * Ogni riga appartiene a un ordine (Codice_Ordine) ed è identificata,
 * all'interno di quell'ordine, da CodiceRiga (stringa, es. "1", "2", "3"...).
 *
 * NOTA SULLO SCHEMA REALE: a differenza di quanto suggerisce il diagramma ER,
 * RigaProdottoMenu e RigaProdottoSingolo NON sono tabelle separate nel database:
 * sono due colonne nullable direttamente su Riga_prodotto. Il vincolo
 * EXTONE_Riga_prodotto impone che, per ogni riga, esattamente una delle due
 * colonne sia valorizzata (con il Codice_Prodotto) e l'altra sia NULL —
 * quindi l'inserimento è un singolo INSERT, non due su tabelle diverse.
 */
public class RigaOrdine {

    private final String codiceOrdine;
    private final String codiceRiga;
    private final int quantita;
    private final float prezzo;
    private final String codiceProdotto;
    private final boolean menu; // true = valorizza RigaProdottoMenu, false = valorizza RigaProdottoSingolo
    private int numeroRiga = 1;

    public RigaOrdine(String codiceOrdine, String codiceRiga, int quantita, float prezzo,
                       String codiceProdotto, boolean menu) {
        this.codiceOrdine = codiceOrdine;
        this.codiceRiga = codiceRiga;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.codiceProdotto = codiceProdotto;
        this.menu = menu;
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public String getCodiceRiga() {
        return codiceRiga;
    }

    public int getQuantita() {
        return quantita;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public boolean isMenu() {
        return menu;
    }

    public int getNumeroRiga() {
        return numeroRiga;
    }

    public void incrementaNumeroRiga() {
        this.numeroRiga++;
    }

    // =========================================================================================
    // DAO
    // =========================================================================================

    public static class DAO {

        private DAO() {
        }

        /**
         * Inserisce una nuova riga prodotto per l'ordine indicato.
         * Il CodiceRiga viene generato automaticamente (scoped sull'ordine, vedi
         * {@link #getNextCodiceRiga}). L'inserimento è un singolo INSERT su
         * Riga_prodotto: la colonna RigaProdottoMenu o RigaProdottoSingolo
         * viene valorizzata con codiceProdotto a seconda del flag "menu",
         * lasciando NULL l'altra, per rispettare il vincolo EXTONE_Riga_prodotto.
         *
         * @return true se l'inserimento è andato a buon fine.
         */
        public static boolean inserisciRigaOrdine(final Connection connection, final String codiceOrdine,
                                                    final String codiceProdotto, final int quantita,
                                                    final float prezzo, final boolean menu, final String codiceRiga) {

            String sql = "INSERT INTO Riga_prodotto " +
                         "(Codice_Ordine, CodiceRiga, Quantita, Prezzo, RigaProdottoMenu, RigaProdottoSingolo) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, codiceOrdine);
                statement.setString(2, codiceRiga);
                statement.setInt(3, quantita);
                statement.setFloat(4, prezzo);

                if (menu) {
                    statement.setString(5, codiceProdotto);
                    statement.setNull(6, Types.VARCHAR);
                } else {
                    statement.setNull(5, Types.VARCHAR);
                    statement.setString(6, codiceProdotto);
                }

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore nell'inserimento della riga ordine", e);
            }
        }

        /**
         * Calcola il prossimo CodiceRiga disponibile per un dato ordine.
         * CodiceRiga è testuale ma rappresenta un numero progressivo (es. "1", "2"...),
         * quindi il calcolo del massimo va fatto numericamente e non lessicograficamente
         * (altrimenti "10" risulterebbe "minore" di "9").
         * Non è un contatore globale: essendo CodiceRiga parte della chiave composta
         * (Codice_Ordine, CodiceRiga), per ogni nuovo ordine il conteggio riparte
         * automaticamente da "1", perché il massimo viene calcolato filtrando
         * su quello specifico Codice_Ordine.
         */
        public static String getNextCodiceRiga(final Connection connection, final String codiceOrdine) {
            String sql = "SELECT CodiceRiga FROM Riga_prodotto WHERE Codice_Ordine = ?";
            int massimo = 0;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, codiceOrdine);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String valore = resultSet.getString("CodiceRiga");
                        try {
                            int numerico = Integer.parseInt(valore.trim());
                            if (numerico > massimo) {
                                massimo = numerico;
                            }
                        } catch (NumberFormatException ignored) {
                            // CodiceRiga non numerico: viene ignorato nel calcolo del massimo
                        }
                    }
                }
                return String.valueOf(massimo + 1);
            } catch (SQLException e) {
                throw new DAOException("Errore nel calcolo del prossimo CodiceRiga", e);
            }
        }

        /**
         * Recupera tutte le righe (con relativo tipo) associate a un ordine,
         * utile ad esempio per ricostruire lo scontrino.
         */
        public static List<RigaOrdine> getRigheByOrdine(final Connection connection, final String codiceOrdine) {
            String sql = "SELECT Codice_Ordine, CodiceRiga, Quantita, Prezzo, RigaProdottoMenu, RigaProdottoSingolo " +
                         "FROM Riga_prodotto WHERE Codice_Ordine = ? ORDER BY CodiceRiga";
            List<RigaOrdine> righe = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, codiceOrdine);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String prodottoMenu = resultSet.getString("RigaProdottoMenu");
                        String prodottoSingolo = resultSet.getString("RigaProdottoSingolo");
                        boolean isMenu = prodottoMenu != null;
                        String codiceProdotto = isMenu ? prodottoMenu : prodottoSingolo;

                        righe.add(new RigaOrdine(
                                resultSet.getString("Codice_Ordine"),
                                resultSet.getString("CodiceRiga"),
                                resultSet.getInt("Quantita"),
                                resultSet.getFloat("Prezzo"),
                                codiceProdotto,
                                isMenu
                        ));
                    }
                }
                return righe;
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero delle righe dell'ordine", e);
            }
        }
    }
}