package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import convention.persistent.Usuario;
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
@PrepareForTest({ActionItemMailSender.class})
public class MailSenderTest {

    ActionItemMailSender mockMailSender;
    ActionItemMailSender mailSender;
    ActionItem actionItem;
    String port = "SMTP_PORT";
    String host = "SMTP_HOST";

    @Before
    public void setUp(){
        mockMailSender = Mockito.mock(ActionItemMailSender.class);
        mailSender = new ActionItemMailSender();
        actionItem = new ActionItem();
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv(port)).thenReturn("587");
        PowerMockito.when(System.getenv(host)).thenReturn("smtp.gmail.com");
    }

    @Test
    public void elActionItemMailSenderNoEnviaElMailSiElActionItemNoTieneDescripcion(){
        actionItem.addObserver(mailSender);
        try{
            mailSender.onSetResponsables(actionItem);
            fail();
        }catch(Exception emptyActionException){
            assertThat(emptyActionException.getMessage()).isEqualTo(mockMailSender.EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    @Test
    public void elActionItemMailSenderNoEnviaMailSiElActionItemNoTieneResponsables(){
        actionItem.addObserver(mailSender);
        actionItem.setDescripcion("Una descripción");
        try {
            mailSender.onSetResponsables(actionItem);
            fail();
        }catch(Exception emptyActionException) {
            assertThat(emptyActionException.getMessage()).isEqualTo(mockMailSender.EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    @Test
    public void unUsuarioConMailNuloOVacioEsResponsableDeUnActionItemPeroNoSeLeEnviaMail(){
        Usuario usuarioSinMail = Usuario.create("pepe", "pepe","pepe", "1000", null);
        Usuario otroUsuarioSinMail = Usuario.create("juan", "juan","juan", "3000", "");

        actionItem.setDescripcion("Una descripcion");
        actionItem.addObserver(mockMailSender);
        actionItem.setResponsables(Arrays.asList(usuarioSinMail, otroUsuarioSinMail));

        Mockito.verify(mockMailSender,times(1)).onSetResponsables(actionItem);
        Mockito.verify(mockMailSender,times(0)).sendMail(actionItem,usuarioSinMail);
        Mockito.verify(mockMailSender,times(0)).sendMail(actionItem,otroUsuarioSinMail);
    }

    @Test
    public void elActionItemMailSenderEnviaMailATodosLosResponsables(){
        actionItem.addObserver(mockMailSender);
        actionItem.setDescripcion("Una descripcion");
        Usuario unUsuario = Usuario.create("pedro", "pedro","pedro","9000","pedro@10pines.com");

        actionItem.setResponsables(Arrays.asList(unUsuario));
        Mockito.verify(mockMailSender,atLeastOnce()).onSetResponsables(actionItem);
    }
}
