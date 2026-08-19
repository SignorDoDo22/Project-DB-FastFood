package project.db.model;

import project.db.data.Categoria;
import java.sql.Connection;
import project.db.data.RigaOrdine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import project.db.data.Cliente;
import project.db.data.InformazioniAggregate;
import project.db.data.Ingrediente;
import project.db.data.ModificaProdotto;
import project.db.data.Ordine;
import project.db.data.Pair;
import project.db.data.Prodotto;
import project.db.data.ProdottoMenu;
import project.db.data.Recensione;
import project.db.data.Rider;

public class ReadingModel {

    private final Connection connection;

    public ReadingModel(final Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
    }

    public List<Prodotto> loadProdotti() {
        return Prodotto.DAO.list(connection);
    }

    public List<String> loadIngredienti(final String Codice_Prodotto) {
        return Ingrediente.DAO.getIngredienti(connection, Codice_Prodotto);
    }

    public List<Recensione> loadRecensioni(final String codiceUtente) {
        return Recensione.DAO.list(connection, codiceUtente);
    }

    public Cliente getCliente(final String email, final String password) {
        return Cliente.DAO.getCliente(connection, email, password);
    }

    public boolean findRider(final String email, final String password) {
        return Rider.DAO.find(connection, email, password);
    }

    public Rider getRider(final String email, final String password) {
        return Rider.DAO.getRider(connection, email, password);
    }

    public boolean findClient(final String email, final String password) {
        return Cliente.DAO.find(connection, email, password);
    }

    public List<Ordine> loadOrdini() {
        return Ordine.DAO.OrdearReady(connection);
    }

    public List<Ordine> loadOrdiniRecensibili(final String codiceUtente) {
        return Ordine.DAO.listOrdiniRecensibili(connection, codiceUtente);
    }

    public Map<Pair<String, Integer>, List<String>> loadIngredientiMenu(final String codiceProdottoMenu) {
        return ProdottoMenu.DAO.getIngredienti(connection, codiceProdottoMenu);
    }

    public Map<String, Integer> loadIngredientiFromRigaCarrello(final String codiceProdotto) {
        return Ingrediente.DAO.getIngredientiWithQuantita(connection, codiceProdotto);
    }

    public boolean isProdottoOrdinato(final String codiceProdotto) {
        return Prodotto.DAO.isStatoOrdinato(connection, codiceProdotto);
    }

    public Map<String, String> trovaMenuCheContengono(final String codiceProdotto) {
        return Prodotto.DAO.trovaMenuCheLoContengono(connection, codiceProdotto);
    }

    public List<Ingrediente> loadIngredientiDisponibili() {
        return Ingrediente.DAO.list(connection);
    }

    public List<Prodotto> loadProdottiDisponibili() {
        return Prodotto.DAO.list(connection);
    }

    public List<String> loadCategorie() {
        List<String> categorie = new ArrayList<>();
        for (Categoria categoria : Categoria.DAO.list(connection)) {
            categorie.add(categoria.getNomeCategoria());
        }
        return categorie;
    }

    public Boolean isProdottoMenu(final String codiceProdotto) {
        return Prodotto.DAO.isProdottoMenu(connection, codiceProdotto);
    }

    public String getNextProdottoCode() {
        return Prodotto.DAO.getProssimoCodice(connection, "PR", 3);
    }

    public String getNextOrdineCode() {
        return Ordine.DAO.getProssimoCodice(connection, "ORD", 3);
    }

    public boolean checkEmailRiderExists(final String email) {
        return Rider.DAO.checkEmailExists(connection, email);
    }

    public boolean checkEmailClientExists(final String email) {
        return Cliente.DAO.checkEmailExists(connection, email);
    }

    public boolean checkCategoriaExists(final String nomeCategoria) {
        return Categoria.DAO.checkNameCategoriaExists(connection, nomeCategoria);
    }

    public Map<Pair<String, String>, Integer> getClassificaProdottiPiuVenduti() {
        return InformazioniAggregate.DAO.getClassificaProdottiPiuVenduti(connection);
    }

    public Map<Pair<String, String>, Pair<Float, Integer>> getClassificaMiglioriRider() {
        return InformazioniAggregate.DAO.getClassificaMiglioriRider(connection);
    }

    public Map<Pair<String, String>, Pair<Integer, Integer>> getRecensioniNegative() {
        return InformazioniAggregate.DAO.getRecensioniNegative(connection);
    }

    public String getCodiceProdottoByNome(String nomeProdotto) {
        return Prodotto.DAO.getCodbyNome(connection, nomeProdotto);
    }

    public String getCodiceIngredienteByNome(String nomeIngrediente) {
        return Ingrediente.DAO.getIngredienteCodebyName(connection, nomeIngrediente);
    }

    public boolean controllaQuantitaIngrediente(String codiceIngrediente, int quantitaRichiesta,
            String nomeProdotto) {
        return ModificaProdotto.DAO.verificaQuantitaIngrediente(connection, codiceIngrediente, quantitaRichiesta,
                getCodiceProdottoByNome(nomeProdotto));
    }

    public List<Ordine> loadOrdiniInPreparazione() {
        return Ordine.DAO.listOrdiniByAdmin(connection);
    }

}
