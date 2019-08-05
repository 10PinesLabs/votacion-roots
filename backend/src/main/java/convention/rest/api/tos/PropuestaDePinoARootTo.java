package convention.rest.api.tos;

import convention.persistent.PropuestaDePinoARoot;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

public class PropuestaDePinoARootTo {

    @CopyFromAndTo(PropuestaDePinoARoot.pino_FIELD)
    private String pino;

    @CopyFromAndTo(PropuestaDePinoARoot.sponsor_FIELD)
    private UserTo sponsor;

    public String getPino() {
        return pino;
    }

    public UserTo getSponsor() {
        return sponsor;
    }

    public void setPino(String pino) {
        this.pino = pino;
    }

    public void setSponsor(UserTo sponsor) {
        this.sponsor = sponsor;
    }
}
