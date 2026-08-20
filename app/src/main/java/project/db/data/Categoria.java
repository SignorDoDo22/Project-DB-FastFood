package project.db.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Categoria {
    private String nomeCategoria;
    private String idCategoria;

    public Categoria(String nomeCategoria, String idCategoria) {
        this.nomeCategoria = nomeCategoria;
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public static class DAO {

        public static List<Categoria> list(final Connection connection) {
            List<Categoria> categorie = new ArrayList<>();

            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_CATEGORIE.get());
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String nomeCategoria = resultSet.getString("Nome");
                    String idCategoria = resultSet.getString("IDCategoria");
                    Categoria categoria = new Categoria(nomeCategoria, idCategoria);
                    categorie.add(categoria);
                }
            } catch (SQLException e) {
                throw new DAOException("Error listing categories", e);
            }

            return categorie;
        }

        public static List<String> getCategorieNames(final Connection connection) {
            List<String> categorieNames = new ArrayList<>();

            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.MOSTRA_CATEGORIE.get());
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String nomeCategoria = resultSet.getString("nome_categoria");
                    categorieNames.add(nomeCategoria);
                }
            } catch (SQLException e) {
                throw new DAOException("Error listing category names", e);
            }

            return categorieNames;
        }

        public static String getCategoryNamebyCod(final Connection connection, final String nomeCategoria) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.GET_CATEGORIA_BY_NAME.get(),
                    nomeCategoria);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getString("IDCategoria");
                }
            } catch (SQLException e) {
                throw new DAOException("Error getting category ID by name", e);
            }

            return null;
        }

        public static boolean insert(final Connection connection, final String nomeCategoria) {

            String idCategoria = getProssimoCodice(connection, "CAT", 2);
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_CATEGORIA.get(),
                    idCategoria, nomeCategoria)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error inserting category", e);
            }
            return true;
        }

        public static String getLast(Connection connection) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_LAST_CATEGORIA.get())) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("IDCategoria");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero dell'ultima categoria", e);
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

        public static boolean checkNameCategoriaExists(Connection connection, String nomeCategoria) {
            try (PreparedStatement preparedStatement = DAOUtils.prepare(connection, Queries.GET_CATEGORIA_BY_NAME.get(),
                    nomeCategoria);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                return resultSet.next();

            } catch (SQLException e) {
                throw new DAOException("Errore nel controllo dell'esistenza del nome della categoria", e);
            }
        }

    }

}
