package convention.rest.api.tos;

import convention.persistent.Usuario;

public class TemaDeReunionConDescripcionTo extends TemaDeReunionTo {
    public static TemaDeReunionConDescripcionTo create(
            Usuario unAutor,
            String unaDuracion,
            String unaObligatoriedad,
            String unTitulo,
            String unaDescripcion) {

        TemaDeReunionConDescripcionTo to = new TemaDeReunionConDescripcionTo();
        to.setIdDeAutor(unAutor.getId());
        to.setAutor(unAutor.getName());
        to.setDuracion(unaDuracion);
        to.setObligatoriedad(unaObligatoriedad);
        to.setTitulo(unTitulo);
        to.setDescripcion(unaDescripcion);
        return to;
    }
}

