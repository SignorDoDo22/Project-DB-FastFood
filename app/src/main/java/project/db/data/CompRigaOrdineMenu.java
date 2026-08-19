package project.db.data;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CompRigaOrdineMenu {

    private String codiceOrdine;
    private String codiceRiga;
    private int NumeroRiga;
    private String codiceProdotto;

    public CompRigaOrdineMenu(String codiceOrdine, String codiceRiga, int numeroRiga, String codiceProdotto) {
        this.codiceOrdine = codiceOrdine;
        this.codiceRiga = codiceRiga;
        this.NumeroRiga = numeroRiga;
        this.codiceProdotto = codiceProdotto;
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public String getCodiceRiga() {
        return codiceRiga;
    }

    public int getNumeroRiga() {
        return NumeroRiga;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public static class DAO {

        public static boolean insertRigaOrdineMenu(Connection connection, int numeroRiga, String codiceOrdine,
                String codiceRiga, String codiceProdotto) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.INSERIRE_COMPONENTE_MENU_ORDINATO.get())) {
                preparedStatement.setString(1, codiceOrdine);
                preparedStatement.setString(2, codiceRiga);
                preparedStatement.setInt(3, numeroRiga);
                preparedStatement.setString(4, codiceProdotto);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Riga ordine menu inserita: " + codiceOrdine + ", " + codiceRiga + ", " + numeroRiga
                        + ", " + codiceProdotto);
                System.out.println("Rows affected: " + rowsAffected);
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Error inserting order line for menu", e);
            }
        }

        public static boolean insertModificaCompMenu(Connection connection, String Codice_Ordine, String Codice_Riga,
                int NumRowCompMenu, String CodiceIngrediente, int quantita, String tipo) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.INSERIRE_MODIFICA_COMPONENTE_MENU.get())) {
                preparedStatement.setString(1, Codice_Ordine);
                preparedStatement.setString(2, Codice_Riga);
                preparedStatement.setInt(3, NumRowCompMenu);
                preparedStatement.setInt(4, quantita);
                preparedStatement.setString(5, CodiceIngrediente);
                preparedStatement.setString(6, tipo);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Error inserting modified order line for menu", e);
            }
        }
    }

}
