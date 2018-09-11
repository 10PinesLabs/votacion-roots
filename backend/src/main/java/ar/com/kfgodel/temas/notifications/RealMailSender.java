package ar.com.kfgodel.temas.notifications;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

public class RealMailSender extends MailSender {
    Mailer mailer;

    public RealMailSender() {
        mailer = MailerBuilder
                .withSMTPServer(System.getenv("SMTP_HOST"),
                        Integer.parseInt(System.getenv("SMTP_PORT")),
                        System.getenv("SMTP_MAIL"),
                        System.getenv("SMTP_PASSWORD"))
                .buildMailer();
    }

    public void sendMail(String userMail, String subject, String descripcion) {
        Email email = EmailBuilder.startingBlank()
                .from("Reminder Action Item", "votacion-roots@10pines.com")
                .to(userMail, userMail)
                .withSubject(subject)
                .withPlainText(descripcion)
                .buildEmail();
        mailer.sendMail(email, true);
    }
}