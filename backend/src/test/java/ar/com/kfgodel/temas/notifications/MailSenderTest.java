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
public class MailSenderTest {

    RealMailSender mockMailSender;
    String port = "SMTP_PORT";
    String host = "SMTP_HOST";

    @Before
    public void setUp(){
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv(port)).thenReturn("587");
        PowerMockito.when(System.getenv(host)).thenReturn("smtp.gmail.com");
        mockMailSender = Mockito.mock(RealMailSender.class);
    }

    @Test
    public void elActionItemMailSenderEnviaMail(){
        mockMailSender.sendMail("unMail", "unAsunto", "unaDescripcion");
        Mockito.verify(mockMailSender,atLeastOnce()).sendMail("unMail", "unAsunto", "unaDescripcion");
    }
}
