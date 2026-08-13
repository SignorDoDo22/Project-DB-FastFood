package project.db.data;

public class ModificaIngrediente<V,K> {

    private final V value;
    private final K key;
    private String tipoOperazione;

    public ModificaIngrediente(V value, K key) {
        this.value = value;
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }

    public String getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazione(String tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }


}
