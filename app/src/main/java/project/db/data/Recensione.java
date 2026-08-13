package project.db.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class Recensione {

    String codice_Recensione;
    String codice_Ordine;
    String testo_Recensione;
    int voto_Rider;
    int voto_Prodotto;

    public Recensione(String codice_Recensione, String codice_Ordine, String testo_Recensione, int voto_Rider, int voto_Prodotto){
        this.codice_Recensione = codice_Recensione;
        this.codice_Ordine = codice_Ordine;
        this.testo_Recensione = testo_Recensione;
        this.voto_Rider = voto_Rider;
        this.voto_Prodotto = voto_Prodotto;
    }

    public static class DAO {
        public static List<Recensione> list(Connection connection, String codiceUtente) {
            List<Recensione> listRecensioni = new ArrayList<>();

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_ORDINI_RECENSIBILI.get())) {
                preparedStatement.setString(1, codiceUtente);
                try (ResultSet result = preparedStatement.executeQuery()) {

                    while (result.next()) {
                        var codiceRecensione = result.getString("codice_recensione");
                        var codiceOrdine = result.getString("codice_ordine");
                        var testoRecensione = result.getString("testo_recensione");
                        var votoRider = result.getInt("voto_rider");
                        var votoProdotto = result.getInt("voto_prodotto");

                        Recensione recensione = new Recensione(codiceRecensione, codiceOrdine, testoRecensione, votoRider, votoProdotto);
                        listRecensioni.add(recensione);
                    }
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante il recupero delle recensioni", e);
            }

            return listRecensioni;
        }

        public static boolean insert(Connection connection, Recensione recensione, String codiceRider) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_RECENSIONE.get())) {
                preparedStatement.setString(1, recensione.codice_Recensione);
                preparedStatement.setString(2, recensione.codice_Ordine);
                preparedStatement.setString(3, recensione.testo_Recensione);
                preparedStatement.setInt(4, recensione.voto_Rider);
                preparedStatement.setInt(5, recensione.voto_Prodotto);

                int rowsAffected = preparedStatement.executeUpdate();

                if(rowsAffected > 0){
                    Rider.DAO.aggiornaRaitingMedio(codiceRider, recensione.voto_Rider, connection);
                }

            } catch (Exception e) {
                throw new DAOException("Errore durante l'inserimento della recensione", e);
            }

            return true;
        }
    }

}
