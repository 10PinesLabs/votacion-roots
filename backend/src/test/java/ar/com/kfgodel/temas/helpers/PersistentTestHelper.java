package ar.com.kfgodel.temas.helpers;

import ar.com.kfgodel.temas.application.Application;
import convention.persistent.*;
import convention.services.*;

import java.util.Arrays;

public class PersistentTestHelper {
    private TestHelper helper = new TestHelper();
    private TemaService temaService;
    private UsuarioService usuarioService;
    private ReunionService reunionService;
    private MinutaService minutaService;
    private TemaGeneralService temaGeneralService;

    public PersistentTestHelper(Application application) {
        reunionService = application.getImplementationFor(ReunionService.class);
        temaService = application.getImplementationFor(TemaService.class);
        temaGeneralService = application.getImplementationFor(TemaGeneralService.class);
        usuarioService = application.getImplementationFor(UsuarioService.class);
        minutaService = application.getImplementationFor(MinutaService.class);
    }

    public Reunion crearUnaReunionConTemasMinuteada() {
        Usuario usuario = crearUnUsuario();
        Reunion unaReunion = helper.unaReunion();

        TemaDeReunion unTema = crearUnTemaDeReunionConDescripcion();
        unTema.setReunion(unaReunion);
        unaReunion.agregarTema(unTema);

        unTema.agregarInteresado(usuario);
        unTema.agregarInteresado(usuario);
        unTema.agregarInteresado(usuario);

        unaReunion.cerrarVotacion();
        unaReunion.marcarComoMinuteada();

        return reunionService.save(unaReunion);
    }

    private TemaDeReunion crearUnTemaDeReunionConDescripcion() {
        return temaService.save(TemaDeReunionConDescripcion.create(
                usuarioService.getAll().stream().findFirst().orElseGet(() -> crearUnUsuario()),
                helper.unaDuracion(),
                ObligatoriedadDeTema.NO_OBLIGATORIO,
                helper.unTitulo(),
                helper.unaDescripcion(), helper.unLink())
        );
    }

    public Reunion crearUnaReunion() {
        Reunion reunion = helper.unaReunion();
        reunionService.save(reunion);
        return reunion;
    }

    public Reunion crearUnaReunionConTemaParaRellenarActionItems(){
        Reunion unaReunion = helper.unaReunion();
        TemaDeReunion temaParaRellenarActionItems = helper.unTemaDeReunionConTitulo(TemaParaRepasarActionItems.TITULO);
        temaParaRellenarActionItems.setReunion(unaReunion);
        unaReunion.agregarTema(temaParaRellenarActionItems);
        reunionService.save(unaReunion);
        return unaReunion;
    }

    public ActionItem crearActionItem() {
        Usuario usuario = usuarioService.getAll().stream().findFirst().orElseGet(() -> crearUnUsuario());
        ActionItem actionItem = new ActionItem(TestHelper.mockActionItemMailSender());
        actionItem.setDescripcion("Tarea a realizar");
        actionItem.setResponsables(Arrays.asList());
        return actionItem;
    }

    public Usuario crearUnUsuario() {
        return usuarioService.save(helper.unUsuario());
    }

    public Reunion crearReunionMinuteada() {
        Reunion reunion = helper.unaReunion();
        reunion.agregarTema(helper.unTemaDeReunion());
        reunion.marcarComoMinuteada();
        return reunionService.save(reunion);
    }

    public Minuta crearMinutaConActionItem(Reunion unaReunion, ActionItem unActionItem) {
        Minuta minuta = Minuta.create(unaReunion);
        minuta.getTemas().get(0).setActionItems(Arrays.asList(unActionItem));
        return minutaService.save(minuta);
    }

    public TemaParaRepasarActionItems crearUnTemaDeReunionParaRepasarActionItems() {
        Usuario usuario = usuarioService.getAll().get(0);

        Reunion primeraReunion = helper.unaReunionConTemas(helper.unTemaDeReunionDe(usuario));
        primeraReunion.marcarComoMinuteada();
        reunionService.save(primeraReunion);
        Minuta minutaDePrimeraReunion = minutaService.save(Minuta.create(primeraReunion));

        TemaParaRepasarActionItems temaParaRepasarActionItems =
            helper.unTemaParaRepasarActionItems(minutaDePrimeraReunion, usuario);
        Reunion segundaReunion =
            helper.unaReunionConTemas(temaParaRepasarActionItems);
        reunionService.save(segundaReunion);

        return temaParaRepasarActionItems;
    }
}
