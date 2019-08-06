package convention.persistent;

public class TemaDeReunionConDescripcion extends TemaDeReunion {
    public static TemaDeReunionConDescripcion create(
            Usuario unAutor,
            DuracionDeTema unaDuracion,
            ObligatoriedadDeTema unaObligatoriedad,
            String unTitulo,
            String unaDescripcion) {

        TemaDeReunionConDescripcion tema = new TemaDeReunionConDescripcion();
        tema.setAutor(unAutor);
        tema.setDuracion(unaDuracion);
        tema.setObligatoriedad(unaObligatoriedad);
        tema.setTitulo(unTitulo);
        tema.setDescripcion(unaDescripcion);
        return tema;
    }
}
