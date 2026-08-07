package project.db.data;

import java.sql.Connection;
import java.util.List;

public class RigaOrdine {
    private String codiceRiga;
    private String codiceOrdine;
    private String codiceSconto;
    private int prezzo;
    private String RigaProdottoSingolo;

    public RigaOrdine(String codiceRiga, String codiceOrdine, String codiceSconto, int prezzo, String RigaProdottoMenu, String RigaProdottoSingolo) {
        this.codiceRiga = codiceRiga;
        this.codiceOrdine = codiceOrdine;
        this.codiceSconto = codiceSconto;
        this.prezzo = prezzo;
        this.RigaProdottoSingolo = RigaProdottoSingolo;
    }

    public static class DAO {

        public String generateCodiceRiga() {

            return null;

        }

        List<RigaOrdine> list(Connection connection, String codiceOrdine) {
            return null;
        }

        void insert(Connection connection, RigaOrdine rigaOrdine) {

        }
    }

}
