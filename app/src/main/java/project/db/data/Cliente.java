package project.db.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class Cliente {

    private String codiceUtente;
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private Date dataDiNascita;
    private String telefono;

    public Cliente(String codiceUtente, String username, String password, String email,
            String nome, String cognome, Date dataDiNascita, String telefono) {
        this.codiceUtente = codiceUtente;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.telefono = telefono;
    }

    public String getCodiceUtente() {
        return codiceUtente;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public String getTelefono() {
        return telefono;
    }

    public static class DAO {

        public static List<Cliente> list(final Connection connection) {
            List<Cliente> utenti = new ArrayList<>();

            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_CLIENTI.get());
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    utenti.add(mapUtente(resultSet));
                }

            } catch (SQLException e) {
                throw new DAOException("Errore durante il recupero degli utenti", e);
            }

            return utenti;
        }

        private static Cliente mapUtente(ResultSet resultSet) throws SQLException {
            return new Cliente(
                    resultSet.getString("Codice_Utente"),
                    resultSet.getString("Username"),
                    resultSet.getString("Password"),
                    resultSet.getString("Email"),
                    resultSet.getString("Nome"),
                    resultSet.getString("Cognome"),
                    resultSet.getDate("Data_di_Nascita"),
                    resultSet.getString("Telefono"));
        }

        public static boolean insert(final Connection connection, Map<String, String> data) {
            try (PreparedStatement nuovoUtente = connection.prepareStatement(Queries.INSERIRE_CLIENTE.get())) {
                nuovoUtente.setString(1, getProssimoCodice(connection, "CU", 4));
                nuovoUtente.setString(2, data.get("username"));
                nuovoUtente.setString(3, data.get("password"));
                nuovoUtente.setString(4, data.get("email"));
                nuovoUtente.setString(5, data.get("nome"));
                nuovoUtente.setString(6, data.get("cognome"));
                nuovoUtente.setDate(7, java.sql.Date.valueOf(data.get("dataDiNascita")));
                nuovoUtente.setString(8, data.get("telefono"));

                int rowsInserted = nuovoUtente.executeUpdate();
                return rowsInserted > 0;

            } catch (final Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public static Cliente getCliente(final Connection connection, final String password, final String email) {

            try (
                    PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                            Queries.CERCA_CLIENTE_PER_Email.get(), password, email);

                    ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    return mapUtente(resultSet);
                } else {
                    return null;
                }

            } catch (final Exception e) {
                throw new DAOException("Errore durante il recupero del cliente", e);
            }
        }

        public static boolean find(final Connection connection, final String email, final String password) {
            try (
                    var statement = DAOUtils.prepare(connection, Queries.CERCA_CLIENTE_PER_Email.get(), email,
                            password);
                    var resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return true;
                }

                return false;

            } catch (final Exception e) {
                throw new DAOException(e);
            }
        }

        public static String getProssimoCodice(Connection connection, String prefisso, int lunghezzaNumero) {
            String ultimoCodice = getNextCodiceCliente(connection);

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

        public static String getNextCodiceCliente(Connection connection) {
            String nextCodiceRider = null;
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection,
                    Queries.MOSTRA_ULTIMO_CLIENTE_CODICE.get());
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    nextCodiceRider = resultSet.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return nextCodiceRider;
        }

        public static boolean checkEmailExists(Connection connection, String email) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.CERCA_CLIENT_EMAIL.get(),
                    email);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                return resultSet.next();

            } catch (SQLException e) {
                throw new DAOException("Errore nel controllo dell'esistenza dell'email del cliente", e);
            }
        }

    }
}