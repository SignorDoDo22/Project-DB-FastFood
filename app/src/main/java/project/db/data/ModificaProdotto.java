package project.db.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import project.db.Queries;
import project.db.controller.DAOUtils;

public class ModificaProdotto {

    private String codiceProdotto;
    private String codiceOrdine;
    private String codiceRiga;
    private String nomeIngrediente;
    private String codiceIngrediente;
    private String tipoModifica;
    private int quantita;

    public ModificaProdotto(int quantita, String codiceProdotto, String codiceRiga, String nomeIngrediente,
            String codiceIngrediente, String tipoModifica, String codiceOrdine) {
        this.codiceProdotto = codiceProdotto;
        this.codiceRiga = codiceRiga;
        this.nomeIngrediente = nomeIngrediente;
        this.codiceIngrediente = codiceIngrediente;
        this.tipoModifica = tipoModifica;
        this.quantita = quantita;
        this.codiceOrdine = codiceOrdine;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public String getCodiceIngrediente() {
        return codiceIngrediente;
    }

    public String getTipoModifica() {
        return tipoModifica;
    }

    public String getCodiceRiga() {
        return codiceRiga;
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public int getQuantita() {
        return quantita;
    }

    public static class DAO {

        public static List<ModificaProdotto> getModificaProdottoByCodiceRiga(Connection connection, String codiceRiga,
                String codiceOrdine) {
            List<ModificaProdotto> modificheProdotto = new ArrayList<>();
            try (
                    var statement = DAOUtils.prepare(connection, Queries.MOSTRA_MODIFICHE_RIGA_SINGOLA.get(),
                            codiceRiga, codiceOrdine);
                    var setResult = statement.executeQuery();) {
                while (setResult.next()) {
                    String codiceProdotto = setResult.getString("Codice_Prodotto");
                    String nomeIngrediente = setResult.getString("Nome_Ingrediente");
                    String codiceIngrediente = setResult.getString("Codice_Ingrediente");
                    String tipoModifica = setResult.getString("Tipo_Modifica");
                    int quantita = setResult.getInt("Quantita");
                    ModificaProdotto modificaProdotto = new ModificaProdotto(quantita, codiceProdotto, codiceRiga,
                            nomeIngrediente, codiceIngrediente, tipoModifica, codiceOrdine);
                    modificheProdotto.add(modificaProdotto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return modificheProdotto;
        }

        public static boolean verificaQuantitaIngrediente(Connection connection, String codiceIngrediente,
                int quantitaRichiesta, String codiceProdotto) {
            try (
                    var statement = DAOUtils.prepare(connection, Queries.MOSTRA_QUANTITA_INGREDIENTE_PRODOTTO.get(),
                            codiceProdotto, codiceIngrediente);
                    var resultSet = statement.executeQuery();) {

                if (!resultSet.next()) {
                    // nessuna riga trovata: l'ingrediente non è associato a quel prodotto
                    return false;
                }

                int quantitaPresente = resultSet.getInt("Quantita");

                if (resultSet.wasNull()) {
                    return false;
                }

                return quantitaPresente >= quantitaRichiesta;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }
}
