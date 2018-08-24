package ar.com.kfgodel.temas.notifications;

public class MailerConfiguration {

    public static ActionItemObserver getMailer() {
        if("DEV".equals(System.getenv("ENVIROMENT"))) return new ActionItemStubMailSender();
        return new ActionItemMailSender();
    }
}
