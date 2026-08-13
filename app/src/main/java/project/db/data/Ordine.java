package project.db.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class Ordine {

    private Date dataCreazione;
    private String ind_via;
    private String ind_Città;
    private String ind_Civico;
    private String codice_Utente;
    private String codice_Ordine;
    private String rig_ordine = "RIG01";

    public Ordine(final Date dataCreazione, final String ind_via, final String ind_Città, final String ind_Civico,
        final String codice_Utente, final String codice_Ordine){

        this.dataCreazione = dataCreazione;
        this.ind_Città = ind_Città;
        this.ind_via = ind_via;
        this.ind_Civico = ind_Civico;
        this.codice_Ordine = codice_Ordine;
        this.codice_Utente = codice_Utente;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public String getIndVia() {
        return ind_via;
    }

    public String getIndCitta() {
        return ind_Città;
    }

    public String getIndCivico() {
        return ind_Civico;
    }

    public String getCodiceUtente() {
        return codice_Utente;
    }

    public String getCodiceOrdine() {
        return codice_Ordine;
    }

    public static class DAO {

        public static List<Ordine> list(Connection connection){

            List<Ordine> listOrdine = new ArrayList<>();

            try( var preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_ORDINI.get());
                ResultSet result = preparedStatement.executeQuery()) {

                while (result.next()) {

                    var dataCreazione = result.getDate("DataCreazione");
                    var indVia = result.getString("Ind_Via");
                    var indCittà = result.getString("Ind_Città");
                    var indCivico = result.getString("Ind_Civico");
                    var codiceUtente = result.getString("Codice_Utente");
                    var codiceordine = result.getString("Codice_Ordine");
                    Ordine ordine = new Ordine(dataCreazione, indVia, indCittà, indCivico, codiceUtente, codiceordine);
                    listOrdine.add(ordine);
                }

            } catch (Exception e) {
                throw new DAOException("Errore nel caricamento degli ordini",e);
            }
            return listOrdine;
        }

        public static boolean inserisciOrdine(Connection connection, Map<String, String> datiDomicilio, String codiceUtente, String codiceOrdine) {

            Date oggi = Date.valueOf(LocalDate.now());

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_ORDINE.get())) {
                preparedStatement.setDate(1, oggi);
                preparedStatement.setString(2, datiDomicilio.get("via"));
                preparedStatement.setString(3, datiDomicilio.get("citta"));
                preparedStatement.setString(4, datiDomicilio.get("civico"));
                preparedStatement.setString(5, codiceOrdine);
                preparedStatement.setString(6, codiceUtente);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore nell'inserimento dell'ordine", e);
            }

            return true;
        }



        public static List<Ordine> OrdearReady(Connection connection){
            List<Ordine> ordiniPronti = new ArrayList<>();

            try(var preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_ORDINI_PRONTI.get());
                ResultSet result = preparedStatement.executeQuery()) {

                while (result.next()) {

                    var dataCreazione = result.getDate("DataCreazione");
                    var indVia = result.getString("Ind_Via");
                    var indCittà = result.getString("Ind_Citta");
                    var indCivico = result.getString("Ind_Civico");
                    var codiceUtente = result.getString("Codice_Utente");
                    var codiceordine = result.getString("Codice_Ordine");
                    Ordine ordine = new Ordine(dataCreazione, indVia, indCittà, indCivico, codiceUtente, codiceordine);
                    System.out.println("Ordine pronto: " + codiceordine + " per l'utente: " + codiceUtente);

                    ordiniPronti.add(ordine);
                }

            } catch (Exception e) {
                throw new DAOException("Errore nel caricamento degli ordini pronti", e);
            }
            return ordiniPronti;
        }

        public static List<Ordine> listOrdiniRecensibili(Connection connection, String codiceUtente) {
            List<Ordine> ordiniRecensibili = new ArrayList<>();

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_ORDINI_RECENSIBILI.get())) {
                preparedStatement.setString(1, codiceUtente);
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while (result.next()) {
                        var dataCreazione = result.getDate("DataCreazione");
                        var indVia = result.getString("Ind_Via");
                        var indCittà = result.getString("Ind_Citta");
                        var indCivico = result.getString("Ind_Civico");
                        var codiceordine = result.getString("Codice_Ordine");
                        Ordine ordine = new Ordine(dataCreazione, indVia, indCittà, indCivico, codiceUtente, codiceordine);
                        ordiniRecensibili.add(ordine);
                    }
                }
            } catch (Exception e) {
                throw new DAOException("Errore nel caricamento degli ordini recensibili", e);
            }
            System.out.println("Numero di ordini recensibili per l'utente " + codiceUtente + ": " + ordiniRecensibili.size());
            return ordiniRecensibili;
        }

        public static boolean inserisciRecensione(Connection connection, String numOrdine, String testoRecensione, int votoOrdine, int votoRider) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_RECENSIONE.get())) {
                preparedStatement.setString(1, numOrdine);
                preparedStatement.setInt(2, votoRider);
                preparedStatement.setString(3, testoRecensione);
                preparedStatement.setInt(4, votoOrdine);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException("Errore nell'inserimento della recensione", e);
            }

            return true;
        }

        public static boolean prendeInCaricoOrdine(Connection connection, String codiceOrdine, String codiceRider) {
            try {
                connection.setAutoCommit(false);

                try (var preparedStatement = DAOUtils.prepare(connection, Queries.AGGIORNA_ORDINE_RIDER.get())) {
                    preparedStatement.setString(1, codiceRider);
                    preparedStatement.setString(2, codiceOrdine);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                // seconda operazione, stessa connessione, stessa transazione
                boolean statoAggiornato = Stato_Ordine.DAO.updateOrdineToInConsegna(connection, codiceOrdine);

                if (!statoAggiornato) {
                    connection.rollback();
                    return false;
                }

                connection.commit(); // solo ora si salva tutto davvero
                return true;

            } catch (Exception e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                throw new DAOException("Errore nell'aggiornamento dell'ordine con il rider", e);
            } finally {
                try {
                    connection.setAutoCommit(true); // ripristina il comportamento di default
                } catch (SQLException ignored) {
                }
            }
        }



        public static String getLast(Connection connection){
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_ULTIMO_ORDINE_CODICE.get())) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Codice_Ordine");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero dell'ultimo ordine", e);
            }
            return null;
        }

        public static String getProssimoCodice(Connection connection, String prefisso, int lunghezzaNumero) {
            String ultimoCodice = getLast(connection);

            int numero;
            if (ultimoCodice == null || ultimoCodice.isBlank()) {
                numero = 1;
            } else {
                String codiceTrim = ultimoCodice.trim();
                String parteNumerica = codiceTrim.substring(prefisso.length());
                numero = Integer.parseInt(parteNumerica) + 1;
            }

            System.out.println("Prossimo codice generato: " + prefisso + String.format("%0" + lunghezzaNumero + "d", numero));
            return prefisso + String.format("%0" + lunghezzaNumero + "d", numero);
        }

    }

}
