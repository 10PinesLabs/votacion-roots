package convention.persistent;

import ar.com.kfgodel.temas.notifications.ActionItemMailSender;
import ar.com.kfgodel.temas.notifications.ActionItemObserver;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fede on 13/07/17.
 */

@Entity
public class ActionItem  extends PersistableSupport {

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Usuario> responsables;
    public static final String responsables_FIELD = "responsables";
    @OneToOne
    private TemaDeMinuta tema;
    public static final String tema_FIELD = "tema";

    @Lob
    private String descripcion;
    public static final String descripcion_FIELD = "descripcion";

    @Transient
    private List<ActionItemObserver> observers = new ArrayList<>();

    public ActionItem() {
        this.addObserver(new ActionItemMailSender());
    }

    public List<Usuario> getResponsables() {
        return responsables;
    }

    public void setResponsables(List<Usuario> responsables) {
        this.responsables = responsables;
        observers.forEach(observer -> observer.onSetResponsables(this));
    }

    public TemaDeMinuta getTema() {
        return tema;
    }

    public void setTema(TemaDeMinuta tema) {
        this.tema = tema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void addObserver(ActionItemObserver unObserver) {
        this.observers.add(unObserver);
    }
    public void removeObserver(ActionItemObserver unObserver){
        this.observers.remove(unObserver);
    }
}
