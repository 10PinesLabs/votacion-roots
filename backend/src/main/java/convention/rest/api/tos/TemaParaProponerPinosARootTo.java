package convention.rest.api.tos;

import convention.persistent.TemaParaProponerPinosARoot;
import convention.persistent.Usuario;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

import java.util.List;

public class TemaParaProponerPinosARootTo extends TemaDeReunionTo {

    @CopyFromAndTo(TemaParaProponerPinosARoot.propuestas_FIELD)
    private List<PropuestaDePinoARootTo> propuestas;

    public TemaParaProponerPinosARootTo() {
    }

    public static TemaParaProponerPinosARootTo create(
            Usuario unAutor,
            String unaDuracion,
            String unaObligatoriedad,
            String unTitulo,
            List<PropuestaDePinoARootTo> unosPropuestasTo) {

        TemaParaProponerPinosARootTo to = new TemaParaProponerPinosARootTo(unosPropuestasTo);
        to.setIdDeAutor(unAutor.getId());
        to.setAutor(unAutor.getName());
        to.setDuracion(unaDuracion);
        to.setObligatoriedad(unaObligatoriedad);
        to.setTitulo(unTitulo);
        return to;
    }

    private TemaParaProponerPinosARootTo(List<PropuestaDePinoARootTo> unosPropuestasTo) {
        propuestas = unosPropuestasTo;
    }

    public List<PropuestaDePinoARootTo> getPropuestas() {
        return propuestas;
    }
}
