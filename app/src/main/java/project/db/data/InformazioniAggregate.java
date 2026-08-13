package project.db.data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InformazioniAggregate {

    public static class DAO{

        public static Map<Pair<String,String>,Integer> getClassificaProdottiPiuVenduti(Connection connection) {
            try(PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.PRODOTTI_MIGLIORI_CLASSIFICA.get());
                ResultSet resultSet = preparedStatement.executeQuery()) {

                Map<Pair<String,String>,Integer> classifica = new HashMap<>();

                while (resultSet.next()) {
                    String codiceProdotto = resultSet.getString("Codice_Prodotto");
                    String nomeProdotto = resultSet.getString("Nome_Prodotto");
                    int quantitaVenduta = resultSet.getInt("Totale_Venduto");
                    Pair<String,String> prodottoKey = new Pair<>(codiceProdotto, nomeProdotto);
                    classifica.put(prodottoKey, quantitaVenduta);
                }

                return classifica;
            } catch (SQLException e) {
                throw new DAOException("Error retrieving product sales ranking", e);
            }
        }

        public static List<String> getClassificaMiglioriRideer() {

            return null; // Placeholder return statement
        }

        public static List<String> getRecensioniNegative(){
            return null;
        }
    }
}
