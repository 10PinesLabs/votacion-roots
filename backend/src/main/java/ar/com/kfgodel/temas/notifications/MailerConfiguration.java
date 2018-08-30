package ar.com.kfgodel.temas.notifications;

public class MailerConfiguration {

    public static MailerObserver getMailer() {
        if("PROD".equals(System.getenv("ENVIROMENT")) || "STG".equals(System.getenv("ENVIROMENT"))) {
            return new ActionItemMailSender();
        }
        return new ActionItemStubMailSender();
    }
}
