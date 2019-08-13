package ar.com.kfgodel.temas.services;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.*;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReunionServiceTest extends TemasServiceTest {

    private TestHelper helper = new TestHelper();
    private Reunion reunionConMinuta;
    private ActionItem actionItem;

    @Override
    public void setUp() {
        super.setUp();
        reunionConMinuta = crearReunionMinuteada();
        actionItem = crearActionItem();
        crearMinutaConActionItem(reunionConMinuta, actionItem);
    }

    private Reunion crearReunionMinuteada() {
        Reunion reunion = helper.unaReunion();
        reunion.agregarTema(helper.unTemaDeReunion());
        reunion.marcarComoMinuteada();
        return reunionService.save(reunion);
    }

    private Minuta crearMinutaConActionItem(Reunion unaReunion, ActionItem unActionItem) {
        Minuta minuta = Minuta.create(unaReunion);
        minuta.getTemas().get(0).setActionItems(List.of(unActionItem));
        return minutaService.save(minuta);
    }

    private ActionItem crearActionItem() {
        Usuario usuario = usuarioService.save(helper.unUsuario());
        ActionItem actionItem = new ActionItem();
        actionItem.setDescripcion("Tarea a realizar");
        actionItem.setResponsables(List.of(usuario));
        return actionItem;
    }

    @Test
    public void puedoPedirLaUltimaReunionCerrada() {
        assertThat(reunionService.getUltimaReunion().getId()).isEqualTo(reunionConMinuta.getId());
    }

    @Test
    public void seCarganLosActionItemsDeLaReunionAnteriorCuandoElTemaExiste() {
        Reunion proximaReunion = reunionService.getProxima();
        proximaReunion.agregarTema(helper.unTemaDeReunionConTitulo("Repasar action items de la root anterior"));
        reunionService.cargarActionItemsDeLaUltimaMinutaSiExisteElTema(proximaReunion);
        reunionService.save(proximaReunion);

        TemaParaRepasarActionItems temaParaRepasarActionItems = (TemaParaRepasarActionItems) proximaReunion.getTemasPropuestos().get(0);
        List<TemaDeMinuta> temasParaRepasar = temaParaRepasarActionItems.getTemasParaRepasar();
        TemaDeMinuta temaConActionItem = temasParaRepasar.get(0);

        assertThat(temasParaRepasar.size()).isEqualTo(1);
        assertThat(temaConActionItem.getActionItems().get(0)).isEqualTo(actionItem);
    }

    @Test
    public void noSeCarganLosActionItemsDeLaReunionAnteriorCuandoElTemaNoExiste(){
        Reunion proximaReunion = reunionService.getProxima();
        reunionService.cargarActionItemsDeLaUltimaMinutaSiExisteElTema(proximaReunion);
        reunionService.save(proximaReunion);

        assertThat(proximaReunion.getTemasPropuestos().size()).isEqualTo(0);
    }
}
