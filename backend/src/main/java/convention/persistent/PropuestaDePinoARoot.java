package convention.persistent;

public class PropuestaDePinoARoot {
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
