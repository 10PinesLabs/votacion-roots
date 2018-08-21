package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionItemNotificationTest {

    @Test
    public void elActionItemNotificationPuedeEnviarMailAUnaCuenta(){
        String mail = "ailen.munoz@10pines.com";

        ActionItemNotification actionItemNotification = new ActionItemNotification();
        ActionItem actionItem = new ActionItem();
        assertThat(actionItemNotification.send(mail, actionItem)).isTrue();
    }

    @Test
    public void elActionItemNotificationPuedeInstanciarseCorrectamente(){

    }

    @Test
    public void elActionItemNotificationPuedeEnviarMailAMasDeUnaCuenta(){

    }
    @Test
    public void elActionItemNotificationEnviaUnMailCuandoSeAgreganResponsables(){

    }

}
