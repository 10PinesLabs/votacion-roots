package ar.com.kfgodel.temas.notifications;

import org.powermock.api.mockito.PowerMockito;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

public class MailerConfiguration {

    public static MailerObserver getMailerObserver() {
        if("PROD".equals(System.getenv("ENVIROMENT")) || "STG".equals(System.getenv("ENVIROMENT"))) {
            return new ActionItemMailSender();
        }
        return new ActionItemStubMailSender();
    }

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
        return PowerMockito.mock(Mailer.class);
    }
}
