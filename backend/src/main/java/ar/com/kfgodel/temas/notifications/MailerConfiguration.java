package ar.com.kfgodel.temas.notifications;

public class MailerConfiguration {

    public static ActionItemObserver getMailer() {
        if("PROD".equals(System.getenv("ENVIROMENT"))) return new ActionItemMailSender();
        return new ActionItemStubMailSender();
    }
}
