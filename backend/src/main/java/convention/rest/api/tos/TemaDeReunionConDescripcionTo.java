package convention.rest.api.tos;

import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.Usuario;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

public class TemaDeReunionConDescripcionTo extends TemaDeReunionTo {

    @CopyFromAndTo(TemaDeReunionConDescripcion.linkDePresentacion_FIELD)
    private String linkDePresentacion;

    public static TemaDeReunionConDescripcionTo create(
            Usuario unAutor,
            String unaDuracion,
            String unaObligatoriedad,
            String unTitulo,
            String unaDescripcion,
            String unLinkDePresentacion) {

        TemaDeReunionConDescripcionTo to = new TemaDeReunionConDescripcionTo();
        to.setIdDeAutor(unAutor.getId());
        to.setAutor(unAutor.getName());
        to.setDuracion(unaDuracion);
        to.setObligatoriedad(unaObligatoriedad);
        to.setTitulo(unTitulo);
        to.setDescripcion(unaDescripcion);
        to.setLinkDePresentacion(unLinkDePresentacion);
        return to;
    }

    public void setLinkDePresentacion(String unLinkDePresentacion) {
        this.linkDePresentacion = unLinkDePresentacion;
    }

    public String getLinkDePresentacion() {
        return linkDePresentacion;
    }
}

