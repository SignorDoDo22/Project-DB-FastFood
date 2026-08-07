package project.db.model;
import java.sql.Connection;
import project.db.data.Cliente;
import project.db.data.Ingrediente;
import project.db.data.Ordine;
import project.db.data.Prodotto;
import project.db.data.ProdottoMenu;
import project.db.data.Rider;
import project.db.data.ProdottoSingolo;
import java.util.List;
import java.util.Map;

public class WritingModel {

    private final Connection connection;

    public WritingModel(Connection connection) {
        this.connection = connection;
    }


    public boolean userRequestRegistration(final Connection connection, Map<String, String> data) {
        return Cliente.DAO.insert(connection, data);
    }

    public boolean aggiornaProdotto(final String codiceProdotto, final String nome, final String descrizione,
                                     final float prezzo, final boolean disponibile) {
        return Prodotto.DAO.aggiornaProdotto(connection, codiceProdotto, nome, descrizione, prezzo, disponibile);
    }


    public void rendiNonDisponibile(final String codiceProdotto) {
        Prodotto.DAO.rendiNonDisponibile(connection, codiceProdotto);
    }

    public void rendiNonDisponibiliMenu(final List<String> codiciMenu) {
        Prodotto.DAO.rendiNonDisponibiliMenu(connection, codiciMenu);
    }

    public void eliminaProdottoSingolo(final String codiceProdotto) {
        Prodotto.DAO.eliminaProdottoSingolo(connection, codiceProdotto);
    }

    public boolean inserisciProdottoSingolo(final String codiceProdotto, final String singolo, final Map<String, String> dataProdotto) {
        return Prodotto.DAO.insert(connection, dataProdotto, singolo, codiceProdotto);
    }

    public boolean inserisciIngredienteNelProdotto(final String codiceProdotto, final String codiceIngrediente, final int quantita) {
        return ProdottoSingolo.DAO.addIngredienteToProdotto(connection, codiceProdotto, codiceIngrediente, quantita);
    }

    public void inserisciRecensione(final String numOrdine, final String testoRecensione, final int votoOrdine, final int votoRider) {
        Ordine.DAO.inserisciRecensione(connection, numOrdine, testoRecensione, votoOrdine, votoRider);
    }

    public boolean createIngrediente(final String nomeIngrediente, final Map<String,Boolean> ingredientiPresentiCheckBox) {
        return Ingrediente.DAO.insert(connection, nomeIngrediente, ingredientiPresentiCheckBox);
    }

    public boolean inserisciRider(final Map<String,String> data) {
        return Rider.DAO.insert(data, connection);
    }

    public boolean prendeInCaricoOrdine(final String codiceOrdine, final String codiceRider) {
        return Ordine.DAO.prendeInCaricoOrdine(connection, codiceOrdine, codiceRider);
    }

    public boolean inserisciMenu(final Map<String,String> dataProdotto, String menu, String codice) {
        return Prodotto.DAO.insert(connection, dataProdotto, menu, codice);
    }

    public boolean inserisciProdottoNelMenu(final String nomeProdotto, final int quantita, String codice) {
        return ProdottoMenu.DAO.addProdottoToMenu(connection, codice, nomeProdotto, quantita);
    }
}
