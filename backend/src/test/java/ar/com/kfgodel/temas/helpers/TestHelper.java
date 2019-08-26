package ar.com.kfgodel.temas.helpers;

import ar.com.kfgodel.temas.domain.TemaParaProponerPinosARootTest;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import convention.persistent.*;
import convention.rest.api.tos.ActionItemTo;
import convention.rest.api.tos.TemaEnCreacionTo;
import convention.rest.api.tos.UserTo;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by sandro on 30/06/17.
 */
public class TestHelper {

    @Inject
    TypeTransformer typeTransformer;

    public TemaDeReunionConDescripcion unTemaObligatorio() {
        TemaDeReunionConDescripcion tema = TemaDeReunionConDescripcion.create();
        tema.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);
        return tema;
    }

    public TemaDeReunionConDescripcion unTemaNoObligatorio() {
        TemaDeReunionConDescripcion tema = TemaDeReunionConDescripcion.create();
        tema.setObligatoriedad(ObligatoriedadDeTema.NO_OBLIGATORIO);
        return tema;
    }

    public TemaDeReunionConDescripcion unTemaAPartirDeUnTemaGeneral() {
        Reunion reunion = Reunion.create(LocalDate.of(2017, 6, 26));
        TemaGeneral temaGeneral = new TemaGeneral();
        return temaGeneral.generarTemaPara(reunion);
    }

    public Reunion unaReunion() {
        return Reunion.create(LocalDate.of(2017, 6, 16));
    }

    public String unPino() {
        return "un pino";
    }

    public String otroPino() {
        return "otro pino";
    }

    public Usuario unUsuario() {
        return Usuario.create("jorge", "usuario", "contra", "id", "email");
    }

    public Usuario otroUsuario() {
        return Usuario.create("carlos", "usuario", "contra", "id", "email");
    }

    public PropuestaDePinoARoot unaPropuestaDeUnPinoARoot() {
        return new PropuestaDePinoARoot(unPino(), unUsuario());
    }

    public PropuestaDePinoARoot unaPropuestaDeOtroPinoARoot() {
        return new PropuestaDePinoARoot(otroPino(), unUsuario());
    }

    public PropuestaDePinoARoot unaPropuestaDeUnPinoARootSponsoreadoPor(Usuario unSponsor, TemaParaProponerPinosARootTest temaParaProponerPinosARootTest) {
        return new PropuestaDePinoARoot(unPino(), unSponsor);
    }

    public TemaDeReunion unTemaDeReunion() {
        return unTemaNoObligatorio();
    }

    public TemaDeReunion unTemaDeReunionConTitulo(String unTitulo) {
        TemaDeReunion unTema = TemaDeReunionConDescripcion.create();
        unTema.setTitulo(unTitulo);
        return unTema;
    }

    public String unTitulo() {
        return "Un título";
    }

    public String unaDescripcion() {
        return "Una descripción";
    }

    public DuracionDeTema unaDuracion() {
        return DuracionDeTema.CORTO;
    }

    public ObligatoriedadDeTema unaObligatoriedad() {
        return ObligatoriedadDeTema.OBLIGATORIO;
    }

    public TemaDeReunionConDescripcion unTemaDeReunionConDescripcion() {
        Usuario autor = unUsuario();
        String titulo = unTitulo();
        String descripcion = unaDescripcion();
        DuracionDeTema duracion = unaDuracion();
        ObligatoriedadDeTema obligatoriedad = unaObligatoriedad();

        return TemaDeReunionConDescripcion.create(autor, duracion, obligatoriedad, titulo, descripcion);
    }

    public TemaParaProponerPinosARoot unTemaParaProponerPinosARoot() {
        TemaParaProponerPinosARoot tema = TemaParaProponerPinosARoot.create(unUsuario());
        tema.agregarPropuesta(unaPropuestaDeUnPinoARoot());
        tema.agregarPropuesta(unaPropuestaDeOtroPinoARoot());
        return tema;
    }

    public TemaDeReunionConDescripcion unTemaParaRellenarConActionItems(){
        Usuario autor = unUsuario();
        String titulo = TemaParaRepasarActionItems.TITULO;
        String descripcion = unaDescripcion();
        DuracionDeTema duracion = unaDuracion();
        ObligatoriedadDeTema obligatoriedad = unaObligatoriedad();

        return TemaDeReunionConDescripcion.create(autor, duracion, obligatoriedad, titulo, descripcion);
    }
    public TemaParaRepasarActionItems unTemaParaRepasarActionItems() {
        TemaParaRepasarActionItems tema = TemaParaRepasarActionItems.create(unaMinuta(), unTemaParaRellenarConActionItems());
        return tema;
    }

    public UserTo unUserTo() {
        UserTo userTo = new UserTo();
        userTo.setName("un nombre");
        userTo.setLogin("un login");
        userTo.setBackofficeId("un backoffice id");
        userTo.setMail("un mail");
        userTo.setCreation("2019-01-01");
        return userTo;
    }

    public Minuta unaMinuta() {
        Reunion unaReunion = unaReunion();
        unaReunion.agregarTema(unTemaDeReunion());
        unaReunion.cerrarVotacion();
        Minuta minuta = Minuta.create(unaReunion);
        minuta.getTemas().get(0).setActionItems(Arrays.asList(unActionItem()));
        return minuta;
    }

    public Usuario unFeche() {
        return Usuario.create("feche", "fecheromero", "123", "sarlnga", "mail@10pines.com");
    }

    public Usuario unSandro() {
        return Usuario.create("sandro", "unSandro", "123", "sarlonga", "mail2@10pines.com");
    }

    public ActionItem unActionItem() {
        ActionItem actionItem = new ActionItem();
        actionItem.setDescripcion("Una cosa para hacer");
        actionItem.setResponsables(Arrays.asList(unUsuario(), otroUsuario()));
        return actionItem;
    }

    public ActionItemTo unActionItemTo(UserTo unUsuario) {
        ActionItemTo unActionItem = new ActionItemTo();
        unActionItem.setDescripcion("Una cosa para hacer");
        unActionItem.setResponsables(Arrays.asList(unUsuario));
        return unActionItem;
    }

    public TemaGeneral unTemaGeneral() {
        return new TemaGeneral();
    }

    public TemaEnCreacionTo unTemaEnCreacionTo(Reunion unaReunion) {
        TemaEnCreacionTo unTemaEnCreacionTo = new TemaEnCreacionTo();
        unTemaEnCreacionTo.setIdDeReunion(unaReunion.getId());
        unTemaEnCreacionTo.setTitulo(unTitulo());
        unTemaEnCreacionTo.setDescripcion(unaDescripcion());
        unTemaEnCreacionTo.setObligatoriedad(convertirA(unaObligatoriedad(), String.class));
        return unTemaEnCreacionTo;
    }

    private <T> T convertirA(Object unObjeto, Class<T> unaClaseDestino) {
        return typeTransformer.transformTo(unaClaseDestino, unObjeto);
    }

    public TemaDeReunion unTemaDeReunionConPrimeraPropuesta(TemaDeReunion unaPrimeraPropuesta) {
        TemaDeReunion unTemaDeReunion = unTemaDeReunion();
        unTemaDeReunion.setPrimeraPropuesta(unaPrimeraPropuesta);
        return unTemaDeReunion;
    }

    public TemaDeReunion unTemaDeReunionConPrimeraPropuestaParaReunion(TemaDeReunion unaPrimeraPropuesta, Reunion unaReunion) {
        TemaDeReunion unTemaDeReunion = unTemaDeReunionConPrimeraPropuesta(unaPrimeraPropuesta);
        unTemaDeReunion.setReunion(unaReunion);
        return unTemaDeReunion;
    }

    public TemaDeReunion unTemaDeReunion(Reunion unaReunion) {
        TemaDeReunion unTemaDeReunion = unTemaDeReunion();
        unTemaDeReunion.setReunion(unaReunion);
        return unTemaDeReunion;
    }
}
