package project.db.data;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stato_Ordine {

    private Time tempo;
    private Date data;
    private String stato;
    private String codiceOrdine;

    public Stato_Ordine(Time tempo, Date data, String stato, String codiceOrdine) {
        this.tempo = tempo;
        this.data = data;
        this.stato = stato;
        this.codiceOrdine = codiceOrdine;
    }

    public Time getTempo() {
        return tempo;
    }

    public Date getData() {
        return data;
    }

    public String getStato() {
        return stato;
    }

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public static class DAO {

        public static List<Stato_Ordine> list(Connection connection, String codiceOrdine) {
            List<Stato_Ordine> statiOrdine = new ArrayList<>();

            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.MOSTRA_STATO_ORDINE.get())) {
                preparedStatement.setString(1, codiceOrdine);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Time tempo = resultSet.getTime("Tempo");
                    Date data = resultSet.getDate("Data");
                    String stato = resultSet.getString("Stato");
                    Stato_Ordine statoOrdine = new Stato_Ordine(tempo, data, stato, codiceOrdine);
                    statiOrdine.add(statoOrdine);
                }
            } catch (SQLException e) {
                throw new DAOException("Error listing order states", e);
            }

            return statiOrdine;
        }

        public static boolean insert(Connection connection, Stato_Ordine statoOrdine) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.INSERIRE_STATO_ORDINE.get())) {
                preparedStatement.setString(1, statoOrdine.getCodiceOrdine());
                preparedStatement.setString(2, statoOrdine.getStato());
                preparedStatement.setDate(3, statoOrdine.getData());
                preparedStatement.setTime(4, statoOrdine.getTempo());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Error inserting order state", e);
            }
        }

        public static boolean updateOrdineStato(Connection connection, String codiceOrdine, String nuovoStato) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.INSERIRE_ORDINE_STATUS.get())) {
                preparedStatement.setString(2, nuovoStato);
                preparedStatement.setString(1, codiceOrdine);
                preparedStatement.setTime(3, new Time(System.currentTimeMillis()));

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DAOException("Error updating order state", e);
            }

        }

    }
}
