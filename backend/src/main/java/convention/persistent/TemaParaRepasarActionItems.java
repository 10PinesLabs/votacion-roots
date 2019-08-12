package convention.persistent;

import java.util.List;

public class TemaParaRepasarActionItems extends TemaDeReunion {
    private List<TemaDeMinuta> temasParaRepasar;

    public static TemaParaRepasarActionItems create(Minuta unaMinuta, TemaDeReunion unTemaDeReunion) {
        TemaParaRepasarActionItems tema = new TemaParaRepasarActionItems();
        tema.setTitulo(unTemaDeReunion.getTitulo());
        tema.setDescripcion(unTemaDeReunion.getDescripcion());
        tema.setObligatoriedad(unTemaDeReunion.getObligatoriedad());
        tema.setReunion(unTemaDeReunion.getReunion());
        tema.setDuracion(unTemaDeReunion.getDuracion());
        tema.setAutor(unTemaDeReunion.getAutor());
        tema.setInteresados(unTemaDeReunion.getInteresados());
        tema.temasParaRepasar = unaMinuta.getTemas();
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
