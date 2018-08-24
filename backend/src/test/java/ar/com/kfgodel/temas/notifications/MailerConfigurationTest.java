package ar.com.kfgodel.temas.notifications;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MailerConfiguration.class})
public class MailerConfigurationTest {
    private String environment = "ENVIROMENT";

    @Before
    public void setUp() {
        PowerMockito.mockStatic(System.class);
    }

    @Test
    public void cuandoElAmbienteEsDevelopmentElMailSenderNoEnviaMails() {
        PowerMockito.when(System.getenv(environment)).thenReturn("DEV");
        assertThat(MailerConfiguration.getMailer()).isInstanceOf(ActionItemStubMailSender.class);
    }

    @Test
    public void cuandoElAmbienteEsStagingElMailSenderNoEnviaMails() {
        PowerMockito.when(System.getenv(environment)).thenReturn("STG");
        assertThat(MailerConfiguration.getMailer()).isInstanceOf(ActionItemStubMailSender.class);
    }

    @Test
    public void cuandoElAmbienteEsProduccionElMailSenderEnviaMails() {
        PowerMockito.when(System.getenv(environment)).thenReturn("PROD");
        assertThat(MailerConfiguration.getMailer()).isInstanceOf(ActionItemMailSender.class);
    }
}
