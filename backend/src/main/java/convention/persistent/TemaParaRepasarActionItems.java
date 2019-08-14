package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TemaParaRepasarActionItems extends TemaDeReunion {

    public static final String TITULO = "Repasar action items de la root anterior";
    public static final String temasParaRepasar_FIELD = "temasParaRepasar";

    @OneToMany
    @JoinColumn(name = "repasar_id")
    private List<TemaDeMinuta> temasParaRepasar = new ArrayList<>();

    public static TemaParaRepasarActionItems create(Minuta unaMinuta, TemaDeReunion unTemaDeReunion) {
        TemaParaRepasarActionItems tema = new TemaParaRepasarActionItems();
        tema.setTitulo(unTemaDeReunion.getTitulo());
        tema.setDescripcion(unTemaDeReunion.getDescripcion());
        tema.setObligatoriedad(unTemaDeReunion.getObligatoriedad());
        tema.setReunion(unTemaDeReunion.getReunion());
        tema.setDuracion(unTemaDeReunion.getDuracion());
        tema.setAutor(unTemaDeReunion.getAutor());
        tema.setInteresados(unTemaDeReunion.getInteresados());
        tema.temasParaRepasar.addAll(unaMinuta.getTemas());
        return tema;
    }

    public List<TemaDeMinuta> getTemasParaRepasar() {
        return temasParaRepasar;
    }

    @Override
    protected TemaDeReunion createCopy() {
        return super.copy();
    }

    @Override
    public Boolean esParaRepasarActionItems() {
        return true;
    }
}
