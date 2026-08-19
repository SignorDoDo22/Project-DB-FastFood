package project.db.data;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.sql.Connection;
import java.sql.SQLException;
import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class ProdottoMenu extends Prodotto {

    private String codiceMenu;

    public ProdottoMenu(final boolean disponibile, final String codice_prodotto,
            final String descrizioneProdotto, final float prezzoOriginario,
            final String nome_prodotto, final String singolo, final String menu) {
        super(disponibile, codice_prodotto, descrizioneProdotto, prezzoOriginario, nome_prodotto, singolo, menu);
        this.codiceMenu = codice_prodotto;
    }

    public String getCodiceMenu() {
        return codiceMenu;
    }

    public static class DAO {

        public static boolean insert(final Connection connection, String codice) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_MENU.get())) {
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore durante l'inserimento del menu", e);
            }
        }

        public static Map<Pair<String, Integer>, List<String>> getIngredienti(final Connection connection,
                final String codiceMenu) {

            Map<Pair<String, Integer>, String> listaProdotti = new LinkedHashMap<>();
            Map<Pair<String, Integer>, List<String>> ingredientiMenu = new LinkedHashMap<>();
            Map<String, List<String>> cacheIngredientiPerProdotto = new HashMap<>();
            int countRig = 1;

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_COMPONENTI_MENU_CATALOGO.get(),
                    codiceMenu);
                    var resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    var codiceProdotto = resultSet.getString("Codice_Prodotto");
                    var nomeProdotto = resultSet.getString("Nome_Prodotto");
                    var quantita = resultSet.getInt("Quantita");

                    for (int i = 0; i < quantita; i++) {
                        listaProdotti.put(new Pair<>(codiceProdotto, countRig++), nomeProdotto);
                    }
                }

                for (var entry : listaProdotti.entrySet()) {
                    Pair<String, Integer> chiave = entry.getKey();
                    String codiceProdotto = chiave.getFirst();
                    String nomeProdotto = entry.getValue();

                    List<String> ingredientiProdotto = cacheIngredientiPerProdotto.computeIfAbsent(
                            codiceProdotto, cp -> Ingrediente.DAO.getIngredienti(connection, cp));
                    System.out.println("Ingredienti per prodotto " + nomeProdotto + ": " + ingredientiProdotto);
                    ingredientiMenu.put(new Pair<>(nomeProdotto, chiave.getSecond()), ingredientiProdotto);
                }

            } catch (SQLException e) {
                throw new DAOException(e);
            }
            return ingredientiMenu;
        }

        public static boolean addProdottoToMenu(final Connection connection, final String codiceMenu,
                final String nomeProdotto, final int quantita) {

            String codiceProdotto = Prodotto.DAO.getCodbyNome(connection, nomeProdotto);

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_COMPOSTO_MENU.get(), codiceMenu,
                    codiceProdotto, quantita)) {
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("PRODOTTO:" + nomeProdotto + " con quantità: " + quantita);
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }

        public static String getLast(Connection connection) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_LAST_PRODOTTO.get())) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Codice_Prodotto");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero dell'ultimo prodotto", e);
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

            return prefisso + String.format("%0" + lunghezzaNumero + "d", numero);
        }

        public static boolean eliminaComprendeMenu(final Connection connection, final String codiceMenu) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.ELIMINA_DA_COMPOSTO_MENU.get(),
                    codiceMenu)) {
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("PRODOTTO ELIMINA COMPRENDE: Eliminazione menu con codice: " + codiceMenu + ", "
                        + rowsAffected + " righe interessate.");
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore durante l'eliminazione del menu", e);
            }
        }

        public static boolean eliminaMenu(final Connection connection, final String codiceMenu) {

            try {

                try (var stmt = DAOUtils.prepare(connection, Queries.ELIMINA_MENU.get())) {
                    stmt.setString(1, codiceMenu);
                    stmt.executeUpdate();
                }
                try (var stmt = DAOUtils.prepare(connection, Queries.ELIMINA_PRODOTTO.get())) {
                    stmt.setString(1, codiceMenu);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DAOException("Errore durante l'eliminazione del menu", e);
            }
            return true;
        }

        public static Map<String, Integer> getProdottiMenuQuantita(final Connection connection,
                final String codiceMenu) {
            Map<String, Integer> prodottiMenuQuantita = new HashMap<>();

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_PRODOTTI_MENU_QUANTITA.get(),
                    codiceMenu);
                    var resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String nomeProdotto = resultSet.getString("Nome_Prodotto");
                    int quantita = resultSet.getInt("Quantita");
                    prodottiMenuQuantita.put(nomeProdotto, quantita);
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero dei prodotti del menu con le relative quantità", e);
            }

            return prodottiMenuQuantita;
        }

    }
}
