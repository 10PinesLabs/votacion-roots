package ar.com.kfgodel.temas.notifications;

import convention.persistent.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RealMailSender.class})
public class ActionItemMailSenderTest {

    RealMailSender mockMailSender;
    ActionItemMailSender actionItemMailSender;
    ActionItem actionItem;
    String port = "SMTP_PORT";
    String host = "SMTP_HOST";

    @Before
    public void setUp(){
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv(port)).thenReturn("587");
        PowerMockito.when(System.getenv(host)).thenReturn("smtp.gmail.com");
        mockMailSender = Mockito.mock(RealMailSender.class);
        actionItemMailSender = new ActionItemMailSender(mockMailSender);
        actionItem = new ActionItem();
        actionItem.removeAllObservers();
        actionItem.addObserver(actionItemMailSender);
    }

    @Test
    public void elActionItemMailSenderNoEnviaElMailSiElActionItemNoTieneDescripcion(){
        try{
            actionItemMailSender.onSetResponsables(actionItem);
            fail();
        }catch(Exception emptyActionException){
            assertThat(emptyActionException.getMessage()).isEqualTo(actionItemMailSender.EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    @Test
    public void elActionItemMailSenderNoEnviaMailSiElActionItemNoTieneResponsables(){
        actionItem.setDescripcion("Una descripción");
        try {
            actionItemMailSender.onSetResponsables(actionItem);
            fail();
        }catch(Exception emptyActionException) {
            assertThat(emptyActionException.getMessage()).isEqualTo(actionItemMailSender.EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    @Test
    public void unUsuarioConMailNuloOVacioEsResponsableDeUnActionItemPeroNoSeLeEnviaMail(){
        Usuario usuarioSinMail = Usuario.create("pepe", "pepe","pepe", "1000", null);
        Usuario otroUsuarioSinMail = Usuario.create("juan", "juan","juan", "3000", "");
        TemaDeMinuta temaDeMinuta = new TemaDeMinuta();
        TemaDeReunion tema = new TemaDeReunion();
        tema.setTitulo("titulo");
        temaDeMinuta.setTema(tema);
        Reunion reunion = new Reunion();
        reunion.setId(1L);
        tema.setReunion(reunion);
        Minuta minuta = new Minuta();
        minuta.setReunion(reunion);
        temaDeMinuta.setMinuta(minuta);
        actionItem.setDescripcion("Una descripcion");
        actionItem.addObserver(actionItemMailSender);
        actionItem.setTema(temaDeMinuta);

        actionItem.setResponsables(Arrays.asList(usuarioSinMail, otroUsuarioSinMail));

        Mockito.verify(mockMailSender,times(0)).sendMail(usuarioSinMail.getMail(), actionItemMailSender.getAsunto(actionItem), actionItemMailSender.getDescripcion(actionItem));
        Mockito.verify(mockMailSender,times(0)).sendMail(otroUsuarioSinMail.getMail(), actionItemMailSender.getAsunto(actionItem), actionItemMailSender.getDescripcion(actionItem));
    }
}
