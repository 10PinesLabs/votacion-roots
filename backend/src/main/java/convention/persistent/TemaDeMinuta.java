package convention.persistent;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

/**
 * Created by fede on 07/07/17.
 */
@Entity
public class TemaDeMinuta extends PersistableSupport {

    public static final String minuta_FIELD = "minuta";
    public static final String actionItems_FIELD = "actionItems";
    public static final String tema_FIELD = "tema";
    public static final String conclusion_FIELD = "conclusion";
    public static final String fueTratado_FIELD = "fueTratado";
    @ManyToOne
    private Minuta minuta;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActionItem> actionItems;
    @OneToOne
    private TemaDeReunion tema;
    private String conclusion;
    private boolean fueTratado;

    public static TemaDeMinuta create(TemaDeReunion temaDeReunion, Minuta minuta) {
        TemaDeMinuta nuevoTema = new TemaDeMinuta();
        nuevoTema.setTema(temaDeReunion);
        nuevoTema.setMinuta(minuta);
        return nuevoTema;
    }

    public TemaDeReunion getTema() {
        return tema;
    }

    public void setTema(TemaDeReunion tema) {
        this.tema = tema;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public List<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<ActionItem> actionItems) {
        if (this.actionItems == null) {
            this.actionItems = actionItems;
        } else {
            this.actionItems.clear();
            this.actionItems.addAll(actionItems);
        }
    }

    public boolean getFueTratado() {
        return fueTratado;
    }

    public void setFueTratado(boolean fueTratado) {
        this.fueTratado = fueTratado;
    }
}
