package ar.com.kfgodel.temas.notifications;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.powermock.api.mockito.PowerMockito.doAnswer;

public class MailerConfiguration {

    public static Logger logger = LoggerFactory.getLogger(Mailer.class);

    public static String getSenderAdress() {
        return "votacion-roots@10pines.com";
    }

    public static Mailer getMailer() {
        if("PROD".equals(System.getenv("ENVIROMENT")) || "STG".equals(System.getenv("ENVIROMENT"))) {
            return MailerBuilder
                    .withSMTPServer(System.getenv("SMTP_HOST"),
                            Integer.parseInt(System.getenv("SMTP_PORT")),
                            System.getenv("SMTP_MAIL"),
                            System.getenv("SMTP_PASSWORD"))
                    .buildMailer();
        }
        return mailerMock();
    }

    private static Mailer mailerMock() {
        Mailer mailerMock = PowerMockito.mock(Mailer.class);
        doAnswer(invocation -> {
            logger.info("El envío de un email está siendo mockeado.");
            return null;
        }).when(mailerMock).sendMail(Mockito.any(Email.class));
        return mailerMock;
    }
}
