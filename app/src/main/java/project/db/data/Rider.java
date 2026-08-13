package project.db.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class Rider {

    private String codiceRider;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String telefono;
    private Date dataDiNascita;
    private int raitingMedio;
    private float guadagnoTotale;


    public Rider(Date dataDiNascita, String codiceRider, String nome, String cognome, String email, String password, String telefono) {
        this.dataDiNascita = dataDiNascita;
        this.codiceRider = codiceRider;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.setGuadagnoTotale(0);
        this.setRatingMedio(0);
    }

    public String getCodiceRider() {
        return codiceRider;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setRatingMedio(int raitingMedio) {
        this.raitingMedio = raitingMedio;
    }

    public int getRatingMedio() {
        return raitingMedio;
    }

    public void setGuadagnoTotale(float guadagnoTotale) {
        this.guadagnoTotale = guadagnoTotale;
    }

    public float getGuadagnoTotale() {
        return guadagnoTotale;
    }


    public static class DAO {

        public static List<Rider> list(final Connection connection) {
            List<Rider> riders = new ArrayList<>();

            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_RIDER.get());
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String codiceRider = resultSet.getString("Codice_Rider");
                    String nome = resultSet.getString("nome");
                    String cognome = resultSet.getString("cognome");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String telefono = resultSet.getString("telefono");
                    Date dataDiNascita = resultSet.getDate("Data_di_Nascita");
                    int raitingMedio = resultSet.getInt("RaitingMedioRider");
                    float guadagnoTotale = resultSet.getFloat("Guadagno");
                    Rider rider = new Rider(dataDiNascita, codiceRider, nome, cognome, email, password, telefono);
                    rider.setGuadagnoTotale(guadagnoTotale);
                    rider.setRatingMedio(raitingMedio);
                    riders.add(rider);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return riders;
        }


        public static boolean find(final Connection connection, final String email, final String password) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.CERCA_RIDER.get(), email, password);
                ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return true;
                }

                return false;

            } catch (final Exception e) {
                throw new DAOException(e);
            }
        }

        public static Rider getRider(final Connection connection, final String email, final String password) {
          try(PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.CERCA_RIDER.get(), email, password);
              ResultSet resultSet = preparedStatement.executeQuery()) {
              if (resultSet.next()) {
                    var codiceRider = resultSet.getString("Codice_Rider");
                    var nome = resultSet.getString("nome");
                    var cognome = resultSet.getString("cognome");
                    var telefono = resultSet.getString("telefono");
                    var dataDiNascita = resultSet.getDate("Data_di_Nascita");
                    var rider = new Rider(dataDiNascita, codiceRider, nome, cognome, email, password, telefono);
                    rider.setRatingMedio(resultSet.getInt("RaitingMedioRider"));
                    rider.setGuadagnoTotale(resultSet.getFloat("Guadagno"));
                    return rider;
              }
              return null;

          } catch (SQLException e) {
              e.printStackTrace();
              return null;
          }
        }


        public static boolean insert(Map<String, String> data, Connection connection) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_RIDER.get())) {

                preparedStatement.setString(1, getProssimoCodice(connection, "RD", 5));
                preparedStatement.setString(2, data.get("username"));
                preparedStatement.setString(3, data.get("password"));
                preparedStatement.setString(4, data.get("email"));
                preparedStatement.setString(5, data.get("nome"));
                preparedStatement.setString(6, data.get("cognome"));
                preparedStatement.setDate(7, java.sql.Date.valueOf(data.get("dataDiNascita")));
                preparedStatement.setString(8, data.get("telefono"));
                preparedStatement.setInt(9, 0); // RaitingMedioRider
                preparedStatement.setFloat(10, 0); // Guadagno
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public static String getProssimoCodice(Connection connection, String prefisso, int lunghezzaNumero) {
            String ultimoCodice = getNextCodiceRider(connection);

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

        public static String getNextCodiceRider(Connection connection) {
            String nextCodiceRider = null;
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_ULTIMO_RIDER_CODICE.get());
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    nextCodiceRider = resultSet.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return nextCodiceRider;
        }



        public static void aggiornaRaitingMedio(String codice_rider, int raiting, Connection connection) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.AGGIORNA_MEDIA_RIDER.get())) {
                preparedStatement.setString(1, codice_rider);
                preparedStatement.setString(2, codice_rider);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void aggiornaGuadagno(float guadagno, Connection connection, String codiceRider) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.AGGIORNA_GUADAGNO_TOTALE_RIDER.get())) {
                preparedStatement.setFloat(1, guadagno);
                preparedStatement.setString(2, codiceRider);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void riderPrendeInCaricoOrdine(String codiceRider, String codiceOrdine, Connection connection) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_DENTRO_ORDINE_RIDER.get())) {
                preparedStatement.setString(1, codiceRider);
                preparedStatement.setString(2, codiceOrdine);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static String getLast(Connection connection){
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_LAST_RIDER_CODICE.get())) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Codice_Rider");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero del codice dell'ultimo rider", e);
            }
            return null;
        }

        public static boolean checkEmailExists(Connection connection, String email) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.CERCA_RIDER_EMAIL.get(), email);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                return resultSet.next();

            } catch (SQLException e) {
                throw new DAOException("Errore nel controllo dell'esistenza dell'email del rider", e);
            }
        }

    }

}
