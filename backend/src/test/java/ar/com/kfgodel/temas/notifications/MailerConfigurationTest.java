package ar.com.kfgodel.temas.notifications;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MailerConfiguration.class, ActionItemMailSender.class})
public class MailerConfigurationTest {
    private String environment = "ENVIROMENT";
    private String port = "SMTP_PORT";
    private String host = "SMTP_HOST";
    @Before
    public void setUp() {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv(port)).thenReturn("587");
        PowerMockito.when(System.getenv(host)).thenReturn("smtp.gmail.com");
    }

    @Test
    public void cuandoElAmbienteEsDevelopmentElMailSenderNoEnviaMails() {
        PowerMockito.when(System.getenv(environment)).thenReturn("DEV");
        assertThat(MailerConfiguration.getMailerObserver()).isInstanceOf(ActionItemStubMailSender.class);
    }

    @Test
    public void cuandoElAmbienteEsStagingElMailSenderEnviaMails() {

        PowerMockito.when(System.getenv(environment)).thenReturn("STG");
        assertThat(MailerConfiguration.getMailerObserver()).isInstanceOf(ActionItemMailSender.class);
    }

    @Test
    public void cuandoElAmbienteEsProduccionElMailSenderEnviaMails() {
        PowerMockito.when(System.getenv(environment)).thenReturn("PROD");
        assertThat(MailerConfiguration.getMailerObserver()).isInstanceOf(ActionItemMailSender.class);
    }
}
