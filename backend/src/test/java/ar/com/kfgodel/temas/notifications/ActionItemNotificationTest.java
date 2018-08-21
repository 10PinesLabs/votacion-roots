package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import convention.persistent.TemaDeMinuta;
import convention.persistent.Usuario;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ActionItemNotificationTest {

    ActionItemNotificator mailer;

    @Before
    public void setUp(){
        mailer = new ActionItemNotificator();
    }

    @Test
    public void elActionItemNotificatorNoEnviaElMailSiElActionItemNoEsValido(){
        ActionItem emptyActionItem = new ActionItem();
        try{
            mailer.notificar(emptyActionItem);
            fail();
        }catch(Exception emptyActionException){
            assertThat(emptyActionException.getMessage()).isEqualTo(mailer.EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    @Test
    public void elActionItemNotificatorNoEnviaElMailSiElActionItemNoTieneResponsables(){
        ActionItem actionItem = new ActionItem();
        actionItem.setDescripcion("Una descripción");
        try {
            mailer.notificar(actionItem);
            fail();
        }catch(Exception emptyActionException){
            assertThat(emptyActionException.getMessage()).isEqualTo(mailer.EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }
}
