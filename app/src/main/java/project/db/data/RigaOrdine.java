package project.db.data;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RigaOrdine {

    private final String codiceOrdine;
    private final String codiceRiga;
    private final int quantita;
    private final float prezzo;
    private final String codiceProdotto;
    private final boolean menu;
    private int numeroRiga = 1;

    public RigaOrdine(String codiceOrdine, String codiceRiga, int quantita, float prezzo,
            String codiceProdotto, boolean menu) {
        this.codiceOrdine = codiceOrdine;
        this.codiceRiga = codiceRiga;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.codiceProdotto = codiceProdotto;
        this.menu = menu;
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public String getCodiceRiga() {
        return codiceRiga;
    }

    public int getQuantita() {
        return quantita;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public boolean isMenu() {
        return menu;
    }

    public int getNumeroRiga() {
        return numeroRiga;
    }

    public void incrementaNumeroRiga() {
        this.numeroRiga++;
    }

    // =========================================================================================
    // DAO
    // =========================================================================================

    public static class DAO {

        private DAO() {
        }

        public static boolean inserisciRigaOrdine(final Connection connection, final String codiceOrdine,
                final String codiceProdotto, final int quantita,
                final float prezzo, final boolean menu, final String codiceRiga) {

            String sql = "INSERT INTO Riga_prodotto " +
                    "(Codice_Ordine, CodiceRiga, Quantita, Prezzo, RigaProdottoMenu, RigaProdottoSingolo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, codiceOrdine);
                statement.setString(2, codiceRiga);
                statement.setInt(3, quantita);
                statement.setFloat(4, prezzo);

                if (menu) {
                    statement.setString(5, codiceProdotto);
                    statement.setNull(6, Types.VARCHAR);
                } else {
                    statement.setNull(5, Types.VARCHAR);
                    statement.setString(6, codiceProdotto);
                }

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore nell'inserimento della riga ordine", e);
            }
        }

        public static List<RigaOrdine> getRigheByOrdine(final Connection connection, final String codiceOrdine) {
            String sql = "SELECT Codice_Ordine, CodiceRiga, Quantita, Prezzo, RigaProdottoMenu, RigaProdottoSingolo " +
                    "FROM Riga_prodotto WHERE Codice_Ordine = ? ORDER BY CodiceRiga";
            List<RigaOrdine> righe = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, codiceOrdine);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String prodottoMenu = resultSet.getString("RigaProdottoMenu");
                        String prodottoSingolo = resultSet.getString("RigaProdottoSingolo");
                        boolean isMenu = prodottoMenu != null;
                        String codiceProdotto = isMenu ? prodottoMenu : prodottoSingolo;

                        righe.add(new RigaOrdine(
                                resultSet.getString("Codice_Ordine"),
                                resultSet.getString("CodiceRiga"),
                                resultSet.getInt("Quantita"),
                                resultSet.getFloat("Prezzo"),
                                codiceProdotto,
                                isMenu));
                    }
                }
                return righe;
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero delle righe dell'ordine", e);
            }
        }

        public static boolean inserireRigaSingolo(String codiceOrdine, String codiceRiga, String codiceProdotto,
                Connection connection) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_RIGAPRODOTTOSINGOLO.get(),
                    codiceOrdine, codiceRiga, codiceProdotto)) {
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }

        public static boolean inserireRigaMenu(String codiceOrdine, String codiceRiga, String codiceProdotto,
                Connection connection) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_RIGAPRODOTTOMENU.get(),
                    codiceOrdine, codiceRiga, codiceProdotto)) {
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }

        public static boolean InserisciCompMenuRiga(Connection connection, String codiceOrdine, String codiceRiga,
                String codiceProdotto, Integer numRowCompMenu) {
            try (var psComponente = DAOUtils.prepare(connection, Queries.INSERIRE_COMPONENTE_MENU_ORDINATO.get())) {
                psComponente.setString(1, codiceOrdine);
                psComponente.setString(2, codiceRiga);
                psComponente.setInt(3, numRowCompMenu);
                psComponente.setString(4, codiceProdotto);
                int rowsAffected = psComponente.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }

    }
}