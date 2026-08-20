package project.db.data;

import java.sql.Connection;

import project.db.Queries;
import project.db.controller.DAOUtils;

public class ModificaProdottoSingolo {

    private String codiceOrdine;
    private String codiceProdotto;
    private String codiceIngrediente;
    private int quantitaIngrediente;
    private String tipomodifica;

    public ModificaProdottoSingolo(String codiceProdotto, String codiceIngrediente, int quantitaIngrediente,
            String tipomodifica) {

        this.codiceProdotto = codiceProdotto;
        this.codiceIngrediente = codiceIngrediente;
        this.quantitaIngrediente = quantitaIngrediente;
        this.tipomodifica = tipomodifica;
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public String getCodiceIngrediente() {
        return codiceIngrediente;
    }

    public int getQuantitaIngrediente() {
        return quantitaIngrediente;
    }

    public String getTipomodifica() {
        return tipomodifica;
    }

    public static class DAO {

        public static boolean insertModificaProdottoSingolo(Connection connection, String codiceOrdine,
                String codiceRiga, String codiceIngrediente, int quantitaIngrediente, String tipomodifica) {
            try (var preparedStatement = DAOUtils.prepare(connection,
                    Queries.INSERIRE_MODIFICA_PRODOTTO_SINGOLO.get())) {
                preparedStatement.setString(1, codiceOrdine);
                preparedStatement.setString(2, codiceRiga);
                preparedStatement.setString(3, codiceIngrediente);
                preparedStatement.setInt(5, quantitaIngrediente);
                preparedStatement.setString(4, tipomodifica);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
