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

    public List<PropuestaDePinoARootTo> getPropuestas() {
        return propuestas;
    }
}
