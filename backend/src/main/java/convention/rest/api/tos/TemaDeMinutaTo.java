package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.TemaDeMinuta;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

import java.util.List;

/**
 * Created by fede on 07/07/17.
 */
public class TemaDeMinutaTo extends PersistableToSupport {

    @CopyFromAndTo(TemaDeMinuta.minuta_FIELD)
    private Long idDeMinuta;

    @CopyFromAndTo(TemaDeMinuta.tema_FIELD)
    private TemaDeReunionTo tema;

    @CopyFromAndTo(TemaDeMinuta.actionItems_FIELD)
    private List<ActionItemTo> actionItems;

    @CopyFromAndTo(TemaDeMinuta.conclusion_FIELD)
    private String conclusion;

    @CopyFromAndTo(TemaDeMinuta.fueTratado_FIELD)
    private Boolean fueTratado;

    public TemaDeReunionTo getTema() {
        return tema;
    }

    public void setTema(TemaDeReunionTo tema) {
        this.tema = tema;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Long getIdDeMinuta() {
        return idDeMinuta;
    }

    public void setIdDeMinuta(Long idDeMinuta) {
        this.idDeMinuta = idDeMinuta;
    }


    public List<ActionItemTo> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<ActionItemTo> actionItems) {
        this.actionItems = actionItems;
    }
    public Boolean getFueTratado() {
        return fueTratado;
    }

    public void setFueTratado(Boolean fueTratado) {
        this.fueTratado = fueTratado;

    }
}
