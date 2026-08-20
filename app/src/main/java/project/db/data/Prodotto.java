package project.db.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import project.db.Queries;
import project.db.controller.DAOException;
import project.db.controller.DAOUtils;

public class Prodotto {

    private boolean disponibile;
    private String codice_prodotto;
    private String descrizioneProdotto;
    private String nome_prodotto;
    private float prezzoOriginario;
    private String singolo;
    private String menu;
    private Map<String, Integer> ingredientiPresenti = new LinkedHashMap<>();

    public Prodotto(final boolean disponibile, final String codice_prodotto,
            final String descrizioneProdotto, final float prezzoOriginario,
            final String nome_prodotto, final String singolo, final String menu) {
        this.codice_prodotto = codice_prodotto;
        this.nome_prodotto = nome_prodotto;
        this.descrizioneProdotto = descrizioneProdotto;
        this.prezzoOriginario = prezzoOriginario;
        this.disponibile = disponibile;
        this.singolo = singolo;
        this.menu = menu;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public String getCodiceProdotto() {
        return codice_prodotto;
    }

    public String getDescrizioneProdotto() {
        return descrizioneProdotto;
    }

    public String getNomeProdotto() {
        return nome_prodotto;
    }

    public float getPrezzoOriginario() {
        return prezzoOriginario;
    }

    public String getSingolo() {
        return singolo;
    }

    public String getMenu() {
        return menu;
    }

    public boolean isSingolo() {
        return singolo != null;
    }

    public boolean isMenu() {
        return menu != null;
    }

    public void modificaIngredientiPresenti(String codiceIngrediente, int quantita) {
        this.ingredientiPresenti.put(codiceIngrediente, quantita);
    }

    public static class DAO {

        public static List<Prodotto> list(final Connection connection) {
            List<Prodotto> catalogo = new ArrayList<>();

            try (
                    var statement = DAOUtils.prepare(connection, Queries.MOSTRA_PRODOTTI.get());
                    var resultSet = statement.executeQuery();) {
                while (resultSet.next()) {
                    var disponibileTesto = resultSet.getString("Disponibile");
                    var disponibile = "S".equals(disponibileTesto);
                    var nome_prodotto = resultSet.getString("Nome_Prodotto");
                    var descrizione_prodotto = resultSet.getString("Descrizione_Prodotto");
                    var prezzo_Originario = resultSet.getFloat("Prezzo_originario");
                    var codice_prodotto = resultSet.getString("Codice_Prodotto");
                    var singolo = resultSet.getString("Singolo");
                    var menu = resultSet.getString("Menu");

                    catalogo.add(new Prodotto(disponibile, codice_prodotto, descrizione_prodotto, prezzo_Originario,
                            nome_prodotto,
                            singolo, menu));
                }

            } catch (SQLException e) {
                throw new DAOException("Errore durante il recupero dei prodotti", e);
            }

            return catalogo;
        }

        public static boolean check(final Connection connection, final String Codice_Prodotto) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.CERCA_PRODOTTO_PER_CODICE.get())) {
                preparedStatement.setString(1, Codice_Prodotto);
                try (var resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (Exception e) {
                throw new DAOException("Errore durante la verifica di esistenza del prodotto", e);
            }
        }

        public static Prodotto getProdotto(final Connection connection, final String codiceProdotto) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.CERCA_PRODOTTO_PER_CODICE.get())) {
                preparedStatement.setString(1, codiceProdotto);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        var disponibileTesto = resultSet.getString("Disponibile");
                        var disponibile = "S".equals(disponibileTesto);
                        var nome_prodotto = resultSet.getString("Nome_Prodotto");
                        var descrizione_prodotto = resultSet.getString("Descrizione_Prodotto");
                        var prezzo_Originario = resultSet.getFloat("Prezzo_originario");
                        var singolo = resultSet.getString("Singolo");
                        var menu = resultSet.getString("Menu");

                        return new Prodotto(disponibile, codiceProdotto, descrizione_prodotto, prezzo_Originario,
                                nome_prodotto,
                                singolo, menu);

                    } else {
                        return null;
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore durante il recupero del prodotto", e);
            }
        }

        public static boolean insert(final Connection connection, Map<String, String> dataProdotto, String tipo,
                String codice) {

            try (var preparedStatement = DAOUtils.prepare(connection, Queries.INSERIRE_PRODOTTO.get())) {
                preparedStatement.setString(1, codice);
                preparedStatement.setString(2, dataProdotto.get("nomeProdotto"));
                preparedStatement.setString(3, dataProdotto.get("descrizione"));
                preparedStatement.setFloat(4, Float.parseFloat(dataProdotto.get("prezzo")));
                preparedStatement.setString(5, "S");
                preparedStatement.setString(6,
                        Categoria.DAO.getCategoryNamebyCod(connection, dataProdotto.get("idCategoria")));
                preparedStatement.setString(7, (tipo == null) ? "S" : null);
                preparedStatement.setString(8, tipo);

                preparedStatement.executeUpdate();

                if (tipo != null) {
                    ProdottoMenu.DAO.insert(connection, codice);
                } else {
                    ProdottoSingolo.DAO.insert(connection, codice);
                }

            } catch (SQLException e) {
                throw new DAOException("Errore durante l'inserimento del prodotto", e);
            }

            return true;
        }

        public static boolean isStatoOrdinato(final Connection connection, final String codiceProdotto) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.VERIFICA_PRODOTTO_ORDINATO.get())) {
                preparedStatement.setString(1, codiceProdotto);
                try (var resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel controllo storico ordini del prodotto", e);
            }
        }

        public static boolean aggiornaProdotto(final Connection connection, final String codiceProdotto,
                final String nome,
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

        /**
         * Rende non disponibili tutti i menu i cui codici sono passati (usato dopo aver
         * avvisato l'Admin).
         */
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

        public static Map<String, String> trovaMenuCheLoContengono(final Connection connection,
                final String codiceProdotto) {
            Map<String, String> menu = new LinkedHashMap<>();
            try (var preparedStatement = DAOUtils.prepare(connection,
                    Queries.TROVA_MENU_CHE_CONTENGONO_PRODOTTO.get())) {
                preparedStatement.setString(1, codiceProdotto);
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        menu.put(resultSet.getString("Codice_Prodotto"), resultSet.getString("Nome_Prodotto"));
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero dei menu collegati", e);
            }
            return menu;
        }

        public static String getLast(Connection connection) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_LAST_PRODOTTO.get())) {
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

        public static String getCodbyNome(final Connection connection, final String nomeProdotto) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.GET_CODICE_PRODOTTO_BY_NAME.get(),
                    nomeProdotto);
                    var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Codice_Prodotto");
                }
            } catch (SQLException e) {
                throw new DAOException("Errore nel recupero del codice prodotto per nome", e);
            }
            return null;
        }

        public static boolean isProdottoMenu(final Connection connection, final String codiceProdotto) {
            try (var preparedStatement = DAOUtils.prepare(connection, Queries.IS_PRODOTTO_MENU.get(), codiceProdotto);
                    var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            } catch (SQLException e) {
                throw new DAOException("Errore nel controllo se il prodotto è un menu", e);
            }
        }

    }
}