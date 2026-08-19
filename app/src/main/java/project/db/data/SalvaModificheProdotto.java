package project.db.data;

import java.util.LinkedHashMap;
import java.util.Map;
import project.db.data.Pair;

public class SalvaModificheProdotto {

   private String codiceRiga;
   private String codiceProdotto;
   private Map<Pair<String,Integer>, String> tipoModifica = new LinkedHashMap<>();


   public SalvaModificheProdotto(String codiceRiga, String codiceProdotto) {
        this.codiceRiga = codiceRiga;
        this.codiceProdotto = codiceProdotto;
    }

    public String getCodiceRiga() {
        return codiceRiga;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public Map<Pair<String,Integer>, String> getTipoModifica() {
        return tipoModifica;
    }

    public void setTipoModifica(Map<Pair<String,Integer>, String> tipoModifica) {
        this.tipoModifica = tipoModifica;
    }

}
