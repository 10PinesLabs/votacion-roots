package convention.persistent;

import ar.com.kfgodel.temas.model.OrdenarPorVotos;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

/**
 * Esta clase representa una reunion de roots con el temario a realizar
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class Reunion extends PersistableSupport {

    public static final String AGREGAR_TEMA_PARA_PROPONER_PINOS_COMO_ROOT_ERROR_MSG = "no se puede agregar un tema para proponer pinos como root";
    public static final String NO_HAY_TEMA_PARA_REPASAR_ACTION_ITEMS_ERROR_MSG = "no hay tema para repasar action items";
    public static final String fecha_FIELD = "fecha";
    public static final String status_FIELD = "status";
    public static final String temasPropuestos_FIELD = "temasPropuestos";
    @NotNull
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private StatusDeReunion status = StatusDeReunion.PENDIENTE;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TemaDeReunion.reunion_FIELD, orphanRemoval = true)
    @OrderBy(TemaDeReunion.prioridad_FIELD)
    private List<TemaDeReunion> temasPropuestos = new ArrayList<>();

    public static Reunion create(LocalDate fecha) {
        Reunion reunion = new Reunion();
        reunion.fecha = fecha;
        reunion.status = StatusDeReunion.PENDIENTE;
        return reunion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<TemaDeReunion> getTemasPropuestos() {
        return temasPropuestos;
    }

    public void setTemasPropuestos(List<TemaDeReunion> temasPropuestos) {
        getTemasPropuestos().clear();
        if (temasPropuestos != null) {
            getTemasPropuestos().addAll(temasPropuestos);
        }
    }

    public StatusDeReunion getStatus() {
        return status;
    }

    public void setStatus(StatusDeReunion status) {
        this.status = status;
    }

    public void cerrarVotacion() {
        this.getTemasPropuestos().sort(Collections.reverseOrder(OrdenarPorVotos.create()));
        for (int i = 0; i < getTemasPropuestos().size(); i++) {
            TemaDeReunion tema = getTemasPropuestos().get(i);
            tema.setPrioridad(i + 1); // Queremos que empiece de 1 la prioridad
        }
        this.status = StatusDeReunion.CERRADA;
    }

    public void reabrirVotacion() {
        this.status = StatusDeReunion.PENDIENTE;
    }

    public Reunion copy() {
        Reunion copia = new Reunion();
        copia.setPersistenceVersion(this.getPersistenceVersion());
        copia.setMomentoDeUltimaModificacion(this.getMomentoDeUltimaModificacion());
        copia.setMomentoDeCreacion(this.getMomentoDeCreacion());
        copia.setId(this.getId());
        copia.setStatus(this.getStatus());
        copia.setFecha(this.getFecha());
        copia.setTemasPropuestos(this.getTemasPropuestos());
        return copia;
    }

    public void agregarTemasGenerales(List<TemaGeneral> temasGenerales) {
        temasGenerales.forEach(this::agregarTemaGeneral);
    }

    public void agregarTemaGeneral(TemaGeneral temaGeneral) {
        TemaDeReunion temaNuevo = temaGeneral.generarTemaPara(this);
        this.agregarTema(temaNuevo);
    }

    public void agregarTema(TemaDeReunion temaNuevo) {
        if (Objects.equals(temaNuevo.getTitulo(), TemaParaProponerPinosARoot.TITULO)) {
            throw new RuntimeException(AGREGAR_TEMA_PARA_PROPONER_PINOS_COMO_ROOT_ERROR_MSG);
        }
        temasPropuestos.add(temaNuevo);
    }

    public void marcarComoMinuteada() {
        this.setStatus(StatusDeReunion.CON_MINUTA);
    }

    public Set<Usuario> usuariosQueVotaron() {
        Set<Usuario> votantes = new HashSet<>();
        getTemasPropuestos().forEach(temaDeReunion -> votantes.addAll(temaDeReunion.getInteresados()));
        return votantes;
    }

    public void proponerPinoComoRoot(String unPino, Usuario unSponsor) {
        PropuestaDePinoARoot propuesta = new PropuestaDePinoARoot(unPino, unSponsor);
        getTemaParaProponerPinosARoot().agregarPropuesta(propuesta);
    }

    private TemaParaProponerPinosARoot getTemaParaProponerPinosARoot() {
        return (TemaParaProponerPinosARoot) getTemasPropuestos().stream()
                .filter(TemaDeReunion::esParaProponerPinosARoot).findFirst()
                .orElseGet(this::crearTemaParaProponerPinosARoot);
    }

    private TemaParaProponerPinosARoot crearTemaParaProponerPinosARoot() {
        TemaParaProponerPinosARoot temaParaProponerPinos = new TemaParaProponerPinosARoot();
        temaParaProponerPinos.setReunion(this);
        temasPropuestos.add(temaParaProponerPinos);
        return temaParaProponerPinos;
    }

    public void cargarSiExisteElTemaParaRepasarActionItemsDe(Minuta unaMinuta) {
        temasPropuestos.stream()
                .filter(unTema -> Objects.equals(unTema.getTitulo(), TemaParaRepasarActionItems.TITULO))
                .findFirst()
                .ifPresent((temaParaRepasarActionItems) -> {
                    TemaParaRepasarActionItems nuevoTemaParaRepasarActionItems = TemaParaRepasarActionItems.create(unaMinuta, temaParaRepasarActionItems);
                    temasPropuestos.set(temasPropuestos.indexOf(temaParaRepasarActionItems), nuevoTemaParaRepasarActionItems);
                });
    }
}
