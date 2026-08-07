package project.db.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class Admin {


    public static class DAO {

        public static boolean aggiornaProdotto(final Connection connection, final String codiceProdotto, final String nome,
                                              final String descrizione, final float prezzo, final boolean disponibile) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.AGGIORNA_PRODOTTO.get())) {
                preparedStatement.setString(1, nome);
                preparedStatement.setString(2, descrizione);
                preparedStatement.setFloat(3, prezzo);
                preparedStatement.setString(4, disponibile ? "S" : "N");
                preparedStatement.setString(5, codiceProdotto);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore durante l'aggiornamento del prodotto", e);
            }
        }

         public static void rendiNonDisponibile(final Connection connection, final String codiceProdotto) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.RENDI_NON_DISPONIBILE.get())) {
                preparedStatement.setString(1, codiceProdotto);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Errore durante l'aggiornamento della disponibilita'", e);
            }
        }

        /** Rende non disponibili tutti i menu i cui codici sono passati (usato dopo aver avvisato l'Admin). */
        public static void rendiNonDisponibiliMenu(final Connection connection, final List<String> codiciMenu) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.RENDI_NON_DISPONIBILE.get())) {
                for (String codice : codiciMenu) {
                    preparedStatement.setString(1, codice);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                throw new DAOException("Errore durante la disabilitazione dei menu collegati", e);
            }
        }

        public static boolean insert(final Connection connection, final String codiceProdotto, final String nomeProdotto, final String idCategoria,
            final String descrizioneProdotto, final float prezzoOriginario, final boolean disponibile, final String singolo, final String menu) {

                try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_PRODOTTO.get())) {
                preparedStatement.setString(1, disponibile ? "S" : "N");
                preparedStatement.setString(2, codiceProdotto);
                preparedStatement.setString(3, nomeProdotto);
                preparedStatement.setFloat(4, prezzoOriginario);
                preparedStatement.setString(5,descrizioneProdotto);
                preparedStatement.setString(6, singolo);
                preparedStatement.setString(7, menu);
                preparedStatement.setString(8,idCategoria);
                if(menu != null){
                    ProdottoMenu.DAO.insert(connection, codiceProdotto);
                }else{
                    ProdottoSingolo.DAO.insert(connection, codiceProdotto);
                }

            } catch (SQLException e) {
                throw new DAOException("Errore durante l'inserimento del prodotto", e);
            }

            return true;
        }


    }
}
