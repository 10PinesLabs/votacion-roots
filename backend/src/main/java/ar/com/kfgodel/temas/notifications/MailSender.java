package ar.com.kfgodel.temas.notifications;

public abstract class MailSender {

    public abstract void sendMail(String userMail, String subject, String descripcion);
}
