package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import convention.persistent.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

public class MailSenderTest {

    ActionItemMailSender mockMailSender;
    ActionItemMailSender mailSender;
    ActionItem actionItem;

    @Before
    public void setUp(){
        mockMailSender = Mockito.mock(ActionItemMailSender.class);
        mailSender = new ActionItemMailSender();
        actionItem = new ActionItem();
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
