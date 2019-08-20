package ar.com.kfgodel.temas.services;

import convention.persistent.ActionItem;
import convention.persistent.Reunion;
import convention.persistent.TemaDeMinuta;
import convention.persistent.TemaParaRepasarActionItems;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReunionServiceTest extends ServiceTest {

    private Reunion reunionConMinuta;
    private ActionItem actionItem;

    @Override
    public void setUp() {
        super.setUp();
        reunionConMinuta = persistentHelper.crearUnaReunionConTemasMinuteada();
        actionItem = persistentHelper.crearActionItem();
        persistentHelper.crearMinutaConActionItem(reunionConMinuta, actionItem);
    }

    @Test
    public void puedoPedirLaUltimaReunionCerrada() {
        Reunion ultimaReunion = reunionService.getUltimaReunion()
                .orElseThrow(() -> new RuntimeException("No hay ultima reunion"));
        assertThat(ultimaReunion.getId()).isEqualTo(reunionConMinuta.getId());
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
    public void noSeCarganLosActionItemsDeLaReunionAnteriorCuandoElTemaNoExiste() {
        Reunion proximaReunion = reunionService.getProxima();
        reunionService.cargarActionItemsDeLaUltimaMinutaSiExisteElTema(proximaReunion);
        reunionService.save(proximaReunion);

        assertThat(proximaReunion.getTemasPropuestos().size()).isEqualTo(0);
    }
}
