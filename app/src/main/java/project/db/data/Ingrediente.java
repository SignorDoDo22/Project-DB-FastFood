package project.db.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class Ingrediente {

    private Boolean vegano;
    private Boolean senzaGlutine;
    private Boolean senzaLattosio;
    private String nomeIngrediente;
    private String codiceIngrediente;

    public Ingrediente(Boolean vegano, Boolean senzaGlutine, Boolean senzaLattosio,
        String nomeIngrediente, String codiceIngrediente){
        this.vegano = vegano;
        this.senzaGlutine = senzaGlutine;
        this.nomeIngrediente = nomeIngrediente;
        this.senzaLattosio = senzaLattosio;
        this.codiceIngrediente = codiceIngrediente;
    }

    public Boolean getVegano() {
        return vegano;
    }

    public Boolean getSenzaGlutine() {
        return senzaGlutine;
    }

    public Boolean getSenzaLattosio() {
        return senzaLattosio;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public String getCodiceIngrediente() {
        return codiceIngrediente;
    }

    public static class DAO {

        public static List<Ingrediente> list(Connection connection){

            List<Ingrediente> ingredienti = new ArrayList<>();
            try(
                var statement = DAOUtils.prepare(connection, Queries.MOSTRA_INGREDIENTI.get());
                var setResult = statement.executeQuery();){

                while (setResult.next()) {
                    var senzaLattosio = setResult.getBoolean("Lattosio");
                    var vegano = setResult.getBoolean("Vegano");
                    var codiceIngrediente = setResult.getString("Codice_Ingrediente");
                    var nomeIngrediente = setResult.getString("Nome_Ingrediente");
                    var senzaGlutine = setResult.getBoolean("Glutine");

                    Ingrediente ingrediente = new Ingrediente(vegano, senzaGlutine, senzaLattosio, nomeIngrediente, codiceIngrediente);
                    ingredienti.add(ingrediente);
                }

            } catch (Exception e) {
                throw new DAOException("Errore nel caricamento degli ingredienti", e);
            }

            System.out.println("PRODOTTO: Lunghezza ingredienti: " + ingredienti.size());
            return ingredienti;
        }

        /**
         * Verifica se un ingrediente con il codice specificato esiste nel DB.
         * FIX: prima il parametro codiceIngrediente non veniva mai passato alla query
         * (mancava il setString), quindi il placeholder "?" restava non valorizzato.
         */
        public static boolean check(Connection connection, String codiceIngrediente){
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.TROVA_INGREDIENTE.get())) {
                preparedStatement.setString(1, codiceIngrediente);
                try (var resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (Exception e) {
                throw new DAOException("Errore Ingrediente specifico non trovato", e);
            }
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

            return prefisso + String.format("%0" + lunghezzaNumero + "d", numero);
        }


        public static boolean insert(final Connection connection, final String nomeIngrediente, final Map<String, Boolean> ingredientiPresentiCheckBox) {

            System.out.println("PRODOTTO: Inserimento ingrediente: " + nomeIngrediente);

            if(check(connection, nomeIngrediente)) {
                System.out.println("PRODOTTO: Ingrediente già presente: " + nomeIngrediente);
                return false;
            }

            String nuovoCodice = getProssimoCodice(connection, "ING", 2); // es. "ING11"
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_INGREDIENTE.get())) {

                preparedStatement.setString(1, nuovoCodice);
                preparedStatement.setString(2, nomeIngrediente);
                preparedStatement.setBoolean(3, ingredientiPresentiCheckBox.getOrDefault("Vegano", false));
                preparedStatement.setBoolean(4, ingredientiPresentiCheckBox.getOrDefault("Lattosio", false));
                preparedStatement.setBoolean(5, ingredientiPresentiCheckBox.getOrDefault("Glutine", false));
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (Exception e) {
                throw new DAOException("Errore durante l'inserimento dell'ingrediente", e);
            }
        }

        public static List<Ingrediente> getIngredientiNonPresenti(Connection connection, String codiceProdotto) {
            List<Ingrediente> ingredienti = new ArrayList<>();
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INGREDIENTI_NON_PRESENTI.get())) {
                preparedStatement.setString(1, codiceProdotto);
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        var senzaLattosio = resultSet.getBoolean("SenzaLattosio");
                        var vegano = resultSet.getBoolean("Vegano");
                        var codiceIngrediente = resultSet.getString("Codice_Ingrediente");
                        var nomeIngrediente = resultSet.getString("Nome_Ingrediente");
                        var senzaGlutine = resultSet.getBoolean("SenzaGlutine");
                        Ingrediente ingrediente = new Ingrediente(vegano, senzaGlutine, senzaLattosio, nomeIngrediente, codiceIngrediente);
                        ingredienti.add(ingrediente);
                    }
                }
            } catch (Exception e) {
                throw new DAOException("Errore nel caricamento degli ingredienti non presenti", e);
            }
            return ingredienti;
        }

        public static Map<String, Integer> getIngredientiWithQuantita(final Connection connection, final String codiceProdotto) {

            Map<String, Integer> listaIngredienti = new HashMap<>();
            System.out.println("PRODOTTO: Recupero ingredienti per prodotto: " + codiceProdotto);
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INGREDIENTI_CONTENUTI_Quantita.get())) {
                preparedStatement.setString(1, codiceProdotto);

                try (var result = preparedStatement.executeQuery()) {
                    while (result.next()) {
                        listaIngredienti.put(result.getString("nome_ingrediente"), result.getInt("quantita"));
                    }
                }

            } catch (SQLException e) {
                throw new DAOException("Errore durante il recupero degli ingredienti del prodotto", e);
            }
            System.out.println("PRODOTTO: Lunghezza ingredienti: " + listaIngredienti.size());
            return listaIngredienti;
        }

        public static List<String> getIngredienti(final Connection connection, final String codiceProdotto) {

            if (!check(connection, codiceProdotto)) {
                return new ArrayList<>();
            }

            List<String> listaIngredienti = new ArrayList<>();

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INGREDIENTI_CONTENUTI.get())) {
                preparedStatement.setString(1, codiceProdotto);

                try (var result = preparedStatement.executeQuery()) {
                    while (result.next()) {
                        listaIngredienti.add(result.getString("nome_ingrediente"));
                    }
                }

            } catch (SQLException e) {
                throw new DAOException("Errore durante il recupero degli ingredienti del prodotto", e);
            }
            System.out.println("PRODOTTO (LISTA): Lunghezza ingredienti: " + listaIngredienti.size());
            return listaIngredienti;
        }

        public static String getLast(Connection connection){
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_LAST_INGREDIENTE.get())) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Codice_Ingrediente");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero dell'ultimo ingrediente", e);
            }
            return null;
        }

        public static String getIngredienteCodebyName(Connection connection, String nomeIngrediente) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_INGREDIENTE_BY_NAME.get(), nomeIngrediente)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Codice_Ingrediente");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero del codice ingrediente per nome", e);
            }
            return null;
        }

    }

}