package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

public class ActionItemNotification {
    private Mailer mailer;
    public ActionItemNotification(){
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "ailen.munoz@10pines.com", "1165932228a.")
                .buildMailer();
    }

    public void send(String mailTo, ActionItem actionItem) {
        Email email = EmailBuilder.startingBlank()
                .from("Action Item Bot", "ailen.munoz@10pines.com" )
                .to(mailTo, mailTo)
                .withSubject("Tenes Action-Items pendientes")
                .withPlainText("Recordá hacerte cargo del Action Item: " + actionItem.getDescripcion())
                .buildEmail();
        mailer.sendMail(email);
    }
}
