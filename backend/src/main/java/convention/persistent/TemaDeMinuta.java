package convention.persistent;

import ar.com.kfgodel.temas.notifications.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Transient
    private List<MailerObserver> observers;

    public static TemaDeMinuta create(TemaDeReunion temaDeReunion, Minuta minuta) {
        MailSender mailerConfiguration = MailerConfiguration.getMailer();
        TemaDeMinuta nuevoTema = new TemaDeMinuta();
        nuevoTema.setTema(temaDeReunion);
        nuevoTema.setMinuta(minuta);
        nuevoTema.setObservers(Arrays.asList(new OnDeleteResponsablesObserver(mailerConfiguration),
                new OnNewActionItemObserver(mailerConfiguration)));
        return nuevoTema;
    }

    private void setObservers(List<MailerObserver> mailerObservers) {
        this.observers = mailerObservers;
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
        List<ActionItem> oldActionItems = this.actionItems;
        if (this.actionItems == null) {
            this.actionItems = actionItems;
        } else {
            this.actionItems.clear();
            this.actionItems.addAll(actionItems);
        }
        actionItems.forEach(actionItem -> actionItem.setTema(this));
        this.observers.forEach(observer -> observer.notificar(oldActionItems, actionItems));
    }

    public boolean getFueTratado() {
        return fueTratado;
    }

    public void setFueTratado(boolean fueTratado) {
        this.fueTratado = fueTratado;
    }
}
