package convention.persistent;

import javax.persistence.Entity;

/**
 * Created by sandro on 03/07/17.
 */
@Entity
public class TemaGeneral extends Tema {

    public TemaDeReunionConDescripcion generarTemaPara(Reunion reunion) {
        TemaDeReunionConDescripcion temaDeReunion = TemaDeReunionConDescripcion.create();
        temaDeReunion.setReunion(reunion);
        temaDeReunion.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);
        temaDeReunion.setTitulo(this.getTitulo());
        temaDeReunion.setDescripcion(this.getDescripcion());
        temaDeReunion.setAutor(this.getAutor());
        temaDeReunion.setDuracion(this.getDuracion());
        temaDeReunion.setTemaGenerador(this);
        temaDeReunion.setUltimoModificador(this.getUltimoModificador());
        return temaDeReunion;
    }

    public void actualizarTema(TemaDeReunion tema) {
        tema.setTitulo(this.getTitulo());
        tema.setDescripcion(this.getDescripcion());
        tema.setDuracion(this.getDuracion());
        tema.setUltimoModificador(this.getUltimoModificador());
    }
}
