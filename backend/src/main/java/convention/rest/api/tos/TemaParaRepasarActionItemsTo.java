package convention.rest.api.tos;

import convention.persistent.TemaParaRepasarActionItems;
import convention.persistent.Usuario;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

import java.util.List;

public class TemaParaRepasarActionItemsTo extends TemaDeReunionTo {

    @TemasParaRepasar
    @CopyFromAndTo(value = TemaParaRepasarActionItems.temasParaRepasar_FIELD,
                   contextAnnotations = TemasParaRepasar.class)
    private List<TemaDeMinutaTo> temasParaRepasar;

    public TemaParaRepasarActionItemsTo() {
    }

    public static TemaParaRepasarActionItemsTo create(Usuario unAutor,
                                                      String unaDuracion,
                                                      String unaObligatoriedad,
                                                      String unTitulo,
                                                      List<TemaDeMinutaTo> temasParaRepasar) {
        TemaParaRepasarActionItemsTo to = new TemaParaRepasarActionItemsTo();
        to.setIdDeAutor(unAutor.getId());
        to.setAutor(unAutor.getName());
        to.setDuracion(unaDuracion);
        to.setObligatoriedad(unaObligatoriedad);
        to.setTitulo(unTitulo);
        to.setTemasParaRepasar(temasParaRepasar);
        return to;
    }

    public List<TemaDeMinutaTo> getTemasParaRepasar() {
        return temasParaRepasar;
    }

    public void setTemasParaRepasar(List<TemaDeMinutaTo> temasParaRepasar) {
        this.temasParaRepasar = temasParaRepasar;
    }
}
