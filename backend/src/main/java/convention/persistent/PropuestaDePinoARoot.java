package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class PropuestaDePinoARoot extends PersistableSupport {

    public static final String pino_FIELD = "pino";
    public static final String sponsor_FIELD = "sponsor";

    @ManyToOne
    private Usuario sponsor;

    private String pino;

    public PropuestaDePinoARoot() {
    }

    public PropuestaDePinoARoot(String unPinoAProponer, Usuario unSponsor) {
        pino = Objects.requireNonNull(unPinoAProponer);
        sponsor = Objects.requireNonNull(unSponsor);
    }

    public String pino() {
        return pino;
    }

    public Usuario sponsor() {
        return sponsor;
    }

    public Boolean correspondeA(String unPino) {
        return pino.equals(unPino);
    }
}
