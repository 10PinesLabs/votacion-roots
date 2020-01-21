package convention.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TemaDeReunionConDescripcion extends TemaDeReunion {
    public static final String linkDePresentacion_FIELD = "linkDePresentacion";

    @Column
    private String linkDePresentacion;

    public static TemaDeReunionConDescripcion create(
            Usuario unAutor,
            DuracionDeTema unaDuracion,
            ObligatoriedadDeTema unaObligatoriedad,
            String unTitulo,
            String unaDescripcion, String unLink) {

        TemaDeReunionConDescripcion tema = new TemaDeReunionConDescripcion();
        tema.setAutor(unAutor);
        tema.setDuracion(unaDuracion);
        tema.setObligatoriedad(unaObligatoriedad);
        tema.setTitulo(unTitulo);
        tema.setDescripcion(unaDescripcion);
        tema.setLinkDePresentacion(unLink);
        return tema;
    }

    public void setLinkDePresentacion(String unLink) {
        this.linkDePresentacion = unLink;
    }

    public static TemaDeReunionConDescripcion create() {
        TemaDeReunionConDescripcion unTema = new TemaDeReunionConDescripcion();
        unTema.setObligatoriedad(ObligatoriedadDeTema.NO_OBLIGATORIO);
        return unTema;
    }

    @Override
    protected TemaDeReunion createCopy() {
        TemaDeReunionConDescripcion copia = new TemaDeReunionConDescripcion();
        copia.setLinkDePresentacion(this.getLinkDePresentacion());
        return copia;
    }

    public String getLinkDePresentacion() {
        return linkDePresentacion;
    }
}
