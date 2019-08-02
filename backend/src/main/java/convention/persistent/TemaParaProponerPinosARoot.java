package convention.persistent;

import java.util.ArrayList;
import java.util.Collection;

public class TemaParaProponerPinosARoot {
    public static final String PINO_YA_PROPUESTO_ERROR_MSG = "el pino ya fue propuesto";
    private Collection<PropuestaDePinoARoot> propuestas = new ArrayList<>();

    public TemaParaProponerPinosARoot(PropuestaDePinoARoot unaPropuesta) {
        agregarPropuesta(unaPropuesta);
    }

    public Collection<PropuestaDePinoARoot> propuestas() {
        return propuestas;
    }

    public void agregarPropuesta(PropuestaDePinoARoot unaPropuesta) {
        verificarQueNoHayaSidoPropuesto(unaPropuesta.pino());
        propuestas.add(unaPropuesta);
    }

    private void verificarQueNoHayaSidoPropuesto(String unPino) {
        if (fuePropuesto(unPino)) {
            throw new RuntimeException(PINO_YA_PROPUESTO_ERROR_MSG);
        }
    }

    private Boolean fuePropuesto(String unPino) {
        return propuestas().stream().anyMatch(propuesta -> propuesta.pino().equals(unPino));
    }
}
