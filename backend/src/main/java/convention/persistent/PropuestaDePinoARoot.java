package convention.persistent;

public class PropuestaDePinoARoot {
    private String pino;

    public PropuestaDePinoARoot(String unPinoAProponer, Usuario unSponsor) {
        pino = unPinoAProponer;
    }

    public String pino() {
        return pino;
    }
}
