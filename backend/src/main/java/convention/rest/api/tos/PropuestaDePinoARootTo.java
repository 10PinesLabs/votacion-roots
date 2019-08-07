package convention.rest.api.tos;

import convention.persistent.PropuestaDePinoARoot;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

public class PropuestaDePinoARootTo {

    @CopyFromAndTo(PropuestaDePinoARoot.pino_FIELD)
    private String pino;

    @CopyFromAndTo(PropuestaDePinoARoot.sponsor_FIELD)
    private UserTo sponsor;

    public PropuestaDePinoARootTo() {
    }

    public PropuestaDePinoARootTo(String pino, UserTo sponsor) {
        this.pino = pino;
        this.sponsor = sponsor;
    }

    public String getPino() {
        return pino;
    }

    public UserTo getSponsor() {
        return sponsor;
    }
}
