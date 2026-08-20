package project.db.data;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;
import java.sql.Connection;
import java.sql.SQLException;

public class ProdottoSingolo extends Prodotto {

    private String codiceIngrediente;

    public ProdottoSingolo(final boolean disponibile, final String codice_prodotto,
            final String descrizioneProdotto, final float prezzoOriginario,
            final String nome_prodotto, final String singolo, final String menu) {
        super(disponibile, codice_prodotto, descrizioneProdotto, prezzoOriginario, nome_prodotto, singolo, menu);
        this.codiceIngrediente = codice_prodotto;
    }

    public String getCodiceIngrediente() {
        return codiceIngrediente;
    }

    public static class DAO {

        public static boolean insert(final Connection connection, String codiceProdotto) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_SINGOLO.get(), codiceProdotto)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            return true;
        }

        public static String getLast(Connection connection) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_LAST_SINGOLO.get())) {
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

        public static boolean addIngredienteToProdotto(final Connection connection, final String codiceProdotto,
                final String nomeIngrediente, int quantita) {

            String codiceIngrediente = Ingrediente.DAO.getIngredienteCodebyName(connection, nomeIngrediente);
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_COMPRENDE.get(),
                    codiceIngrediente, codiceProdotto, quantita)) {
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }

        public static void eliminaProdottoSingolo(final Connection connection, final String codiceProdotto) {
            try {
                connection.setAutoCommit(false);

                try (var stmt = DAOUtils.prepare(connection, Queries.ELIMINA_DA_COMPOSTO_MENU.get())) {
                    stmt.setString(1, codiceProdotto);
                    stmt.executeUpdate();
                }
                try (var stmt = DAOUtils.prepare(connection, Queries.ELIMINA_RICETTA.get())) {
                    stmt.setString(1, codiceProdotto);
                    stmt.executeUpdate();
                }
                try (var stmt = DAOUtils.prepare(connection, Queries.ELIMINA_PRODOTTO_SINGOLO.get())) {
                    stmt.setString(1, codiceProdotto);
                    stmt.executeUpdate();
                }
                try (var stmt = DAOUtils.prepare(connection, Queries.ELIMINA_PRODOTTO.get())) {
                    stmt.setString(1, codiceProdotto);
                    stmt.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                throw new DAOException("Errore durante l'eliminazione del prodotto", e);

            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
