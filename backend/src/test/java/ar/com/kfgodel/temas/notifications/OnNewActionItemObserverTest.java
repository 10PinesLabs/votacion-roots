package ar.com.kfgodel.temas.notifications;

import convention.persistent.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;

public class OnNewActionItemObserverTest {

    RealMailSender mockMailSender;
    OnNewActionItemObserver onNewActionItemObserver;
    List<ActionItem> oldActionItems;
    List<ActionItem> newActionItems;
    Usuario unUsuario;
    Usuario otroUsuario;
    TemaDeMinuta temaDeMinuta;
    TemaDeReunion tema;
    Reunion reunion;

    @Before
    public void setUp(){
        temaDeMinuta = new TemaDeMinuta();
        tema = new TemaDeReunion();
        tema.setTitulo("titulo");
        temaDeMinuta.setTema(tema);
        reunion = new Reunion();
        reunion.setId(1L);
        tema.setReunion(reunion);
        Minuta minuta = new Minuta();
        minuta.setReunion(reunion);
        temaDeMinuta.setMinuta(minuta);

        mockMailSender = Mockito.mock(RealMailSender.class);
        onNewActionItemObserver = new OnNewActionItemObserver(mockMailSender);

        unUsuario = Usuario.create("juan","juan","juan","1","hola@10pines.com");
        otroUsuario = Usuario.create("pedro","pedro","pedro","2", "chau@10pines.com");

        ActionItem unActionItem = new ActionItem();
        unActionItem.setId(1L);
        unActionItem.setDescripcion("una descripcion");
        unActionItem.setResponsables(Arrays.asList(unUsuario));
        unActionItem.setTema(temaDeMinuta);

        ActionItem otroActionItem = new ActionItem();
        otroActionItem.setDescripcion("una descripcion");
        otroActionItem.setResponsables(Arrays.asList(unUsuario));
        otroActionItem.setId(2L);
        otroActionItem.setTema(temaDeMinuta);

        oldActionItems=Arrays.asList(unActionItem, otroActionItem);
    }

    @Test
    public void siLosActionItemsViejosSonIgualesALosNuevosNoSeEnviaMail(){
        onNewActionItemObserver.notificar(oldActionItems, oldActionItems);
        Mockito.verify(mockMailSender,times(0)).sendMail(unUsuario.getMail(),"","");
    }

    @Test
    public void siCambiaLaDescripcionDeUnActionItemSeEnviaMailALosResponsables(){
        ActionItem unTercerActionItem = new ActionItem();
        unTercerActionItem.setDescripcion("descripcionDistinta");
        unTercerActionItem.setId(1L);
        unTercerActionItem.setResponsables(Arrays.asList(unUsuario));
        unTercerActionItem.setTema(temaDeMinuta);
        newActionItems = Arrays.asList(unTercerActionItem);

        onNewActionItemObserver.notificar(oldActionItems, newActionItems);
        Mockito.verify(mockMailSender,times(1)).sendMail(unUsuario.getMail(),onNewActionItemObserver.getAsunto(unTercerActionItem), onNewActionItemObserver.getDescripcion(unTercerActionItem));
    }

    @Test
    public void siCambianLosResponsalesDeUnActionItemSeEnviaMailALosResponsables(){
        ActionItem unTercerActionItem = new ActionItem();
        unTercerActionItem.setDescripcion("una descripcion");
        unTercerActionItem.setId(1L);
        unTercerActionItem.setResponsables(Arrays.asList(unUsuario, otroUsuario));
        unTercerActionItem.setTema(temaDeMinuta);

        newActionItems = Arrays.asList(unTercerActionItem);
        onNewActionItemObserver.notificar(oldActionItems, newActionItems);
        Mockito.verify(mockMailSender,times(1)).sendMail(unUsuario.getMail(),onNewActionItemObserver.getAsunto(unTercerActionItem), onNewActionItemObserver.getDescripcion(unTercerActionItem));
        Mockito.verify(mockMailSender,times(1)).sendMail(otroUsuario.getMail(),onNewActionItemObserver.getAsunto(unTercerActionItem), onNewActionItemObserver.getDescripcion(unTercerActionItem));
    }

    @Test
    public void unUsuarioConMailNuloOVacioEsResponsableDeUnActionItemPeroNoSeLeEnviaMail(){
        Usuario usuarioSinMail = Usuario.create("pepe", "pepe","pepe", "1000", null);
        Usuario otroUsuarioSinMail = Usuario.create("juan", "juan","juan", "3000", "");
        ActionItem actionItem = new ActionItem();
        actionItem.setDescripcion("Una descripcion");
        actionItem.setTema(temaDeMinuta);
        actionItem.setResponsables(Arrays.asList(usuarioSinMail, otroUsuarioSinMail));

        Mockito.verify(mockMailSender,times(0)).sendMail(usuarioSinMail.getMail(), onNewActionItemObserver.getAsunto(actionItem), onNewActionItemObserver.getDescripcion(actionItem));
        Mockito.verify(mockMailSender,times(0)).sendMail(otroUsuarioSinMail.getMail(), onNewActionItemObserver.getAsunto(actionItem), onNewActionItemObserver.getDescripcion(actionItem));
    }

}
