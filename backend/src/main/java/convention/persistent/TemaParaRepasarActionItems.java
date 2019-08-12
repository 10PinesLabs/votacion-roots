package convention.persistent;

import java.util.List;

public class TemaParaRepasarActionItems extends TemaDeReunion {
    private List<TemaDeMinuta> temasParaRepasar;

    public TemaParaRepasarActionItems(Minuta unaMinuta, TemaDeReunion unTemaDeReunion) {
        setTitulo(unTemaDeReunion.getTitulo());
        setDescripcion(unTemaDeReunion.getDescripcion());
        setObligatoriedad(unTemaDeReunion.getObligatoriedad());
        setReunion(unTemaDeReunion.getReunion());
        setDuracion(unTemaDeReunion.getDuracion());
        setAutor(unTemaDeReunion.getAutor());
        setInteresados(unTemaDeReunion.getInteresados());
        temasParaRepasar = unaMinuta.getTemas();
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
