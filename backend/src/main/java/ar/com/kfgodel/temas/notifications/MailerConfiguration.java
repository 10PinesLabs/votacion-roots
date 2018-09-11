package ar.com.kfgodel.temas.notifications;

public class MailerConfiguration {

    public static MailSender getMailer() {
        if("PROD".equals(System.getenv("ENVIROMENT")) || "STG".equals(System.getenv("ENVIROMENT"))) {
            return new RealMailSender();
        }
        return new StubMailSender();
    }
}
