package ar.com.kfgodel.temas.helpers;

import ar.com.kfgodel.temas.application.Application;
import convention.persistent.*;
import convention.services.*;

import java.util.List;

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

    public Reunion crearUnaReunionConTemas() {
        Reunion unaReunion = helper.unaReunion();
        TemaDeReunion unTema = TemaDeReunionConDescripcion.create();
        unTema.setReunion(unaReunion);
        unaReunion.agregarTema(unTema);
        reunionService.save(unaReunion);
        return unaReunion;
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
        Usuario usuario = usuarioService.save(helper.unUsuario());
        ActionItem actionItem = new ActionItem();
        actionItem.setDescripcion("Tarea a realizar");
        actionItem.setResponsables(List.of(usuario));
        return actionItem;
    }

    public Reunion crearReunionMinuteada() {
        Reunion reunion = helper.unaReunion();
        reunion.agregarTema(helper.unTemaDeReunion());
        reunion.marcarComoMinuteada();
        return reunionService.save(reunion);
    }

    public Minuta crearMinutaConActionItem(Reunion unaReunion, ActionItem unActionItem) {
        Minuta minuta = Minuta.create(unaReunion);
        minuta.getTemas().get(0).setActionItems(List.of(unActionItem));
        return minutaService.save(minuta);
    }

}
