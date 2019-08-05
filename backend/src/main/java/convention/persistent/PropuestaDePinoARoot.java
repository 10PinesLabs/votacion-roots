package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PropuestaDePinoARoot extends PersistableSupport {

    @ManyToOne
    private Usuario sponsor;

    private String pino;

    public PropuestaDePinoARoot(String unPinoAProponer, Usuario unSponsor) {
        pino = unPinoAProponer;
        sponsor = unSponsor;
    }

    public String pino() {
        return pino;
    }

    public Usuario sponsor() {
        return sponsor;
    }
}
