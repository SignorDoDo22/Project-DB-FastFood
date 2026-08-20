package project.db.data;

import java.util.HashMap;
import java.util.Map;
import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InformazioniAggregate {

    public static class DAO {

        public static Map<Pair<String, String>, Integer> getClassificaProdottiPiuVenduti(Connection connection) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.PRODOTTI_MIGLIORI_CLASSIFICA.get());
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                Map<Pair<String, String>, Integer> classifica = new HashMap<>();

                while (resultSet.next()) {
                    String codiceProdotto = resultSet.getString("Codice_Prodotto");
                    String nomeProdotto = resultSet.getString("Nome_Prodotto");
                    int quantitaVenduta = resultSet.getInt("Totale_Venduto");
                    Pair<String, String> prodottoKey = new Pair<>(codiceProdotto, nomeProdotto);
                    classifica.put(prodottoKey, quantitaVenduta);
                }

                return classifica;
            } catch (SQLException e) {
                throw new DAOException("Error retrieving product sales ranking", e);
            }
        }

        public static Map<Pair<String, String>, Pair<Float, Integer>> getClassificaMiglioriRider(
                Connection connection) {

            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.CLASSIFICA_MIGLIORI_RIDER.get());
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                Map<Pair<String, String>, Pair<Float, Integer>> classifica = new HashMap<>();

                while (resultSet.next()) {
                    String nomeRider = resultSet.getString("Nome");
                    String cognomeRider = resultSet.getString("Cognome");
                    float punteggio = resultSet.getFloat("RaitingMedioRider");
                    int numeroRecensioni = resultSet.getInt("Guadagno");
                    Pair<String, String> riderKey = new Pair<>(nomeRider, cognomeRider);
                    Pair<Float, Integer> riderStats = new Pair<>(punteggio, numeroRecensioni);
                    classifica.put(riderKey, riderStats);
                }

                return classifica;
            } catch (SQLException e) {
                throw new DAOException("Error retrieving best riders ranking", e);
            }
        }

        public static Map<Pair<String, String>, Pair<Integer, Integer>> getRecensioniNegative(Connection connection) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.MOSTRA_RECENSIONI_NEGATIVE.get());
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                Map<Pair<String, String>, Pair<Integer, Integer>> recensioniNegative = new HashMap<>();

                while (resultSet.next()) {
                    String codiceProdotto = resultSet.getString("Codice_Ordine");
                    String descrizioneString = resultSet.getString("Testo_Recensione");
                    int punteggioOrdine = resultSet.getInt("Voto_Ordine");
                    int punteggioRider = resultSet.getInt("Voto_Rider");
                    Pair<String, String> prodottoKey = new Pair<>(codiceProdotto, descrizioneString);
                    Pair<Integer, Integer> prodottoStats = new Pair<>(punteggioOrdine, punteggioRider);
                    recensioniNegative.put(prodottoKey, prodottoStats);
                }

                return recensioniNegative;
            } catch (SQLException e) {
                throw new DAOException("Error retrieving negative reviews", e);
            }

        }
    }
}
