package convention.persistent;

import ar.com.kfgodel.temas.notifications.MailerObserver;
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
    private Boolean fueNotificado;

    public ActionItem() {
        this.fueNotificado =true;
    }

    public List<Usuario> getResponsables() {
        return responsables;
    }

    public void setResponsables(List<Usuario> responsables) {
        this.responsables = responsables;
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

    public void setFueNotificado(Boolean fueNotificado) {
        this.fueNotificado=fueNotificado;
    }

    public boolean getFueNotificado() {
        return this.fueNotificado;
    }

    public boolean equals(ActionItem unActionItem){
        return this.getDescripcion().equals(unActionItem.getDescripcion())
                && this.getId() == unActionItem.getId()
                && this.getResponsables().containsAll(unActionItem.getResponsables())
                && this.getResponsables().size() == unActionItem.getResponsables().size();
    }

}
