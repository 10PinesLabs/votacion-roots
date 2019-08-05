package convention.persistent;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class TemaParaProponerPinosARoot extends TemaDeReunion {

    public static final String PINO_YA_PROPUESTO_ERROR_MSG = "el pino ya fue propuesto";
    public static final String TITULO = "Proponer pinos a root";

    @OneToMany
    private Collection<PropuestaDePinoARoot> propuestas = new ArrayList<>();

    public TemaParaProponerPinosARoot() {

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

    public String getTitulo() {
        return TITULO;
    }

    public ObligatoriedadDeTema getObligatoriedad() {
        return ObligatoriedadDeTema.OBLIGATORIO;
    }

    public DuracionDeTema getDuracion() {
        return DuracionDeTema.CORTO;
    }

    public Boolean esParaProponerPinosARoot() {
        return true;
    }
}
