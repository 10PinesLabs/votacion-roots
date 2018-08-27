package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import convention.persistent.Usuario;
import org.junit.platform.commons.util.StringUtils;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

import java.util.Optional;

public class ActionItemMailSender extends MailerObserver {
    public static final String EMPTY_ITEM_ACTION_EXCEPTION = "El item debe tener descripción y responsables";
    private Mailer mailer;

    public ActionItemMailSender(){
        mailer = MailerBuilder
                .withSMTPServer(System.getenv("SMTP_HOST"),
                        Integer.parseInt(System.getenv("SMTP_PORT")),
                        System.getenv("SMTP_MAIL"),
                        System.getenv("SMTP_PASSWORD"))
                .buildMailer();
    }

    public void sendMail(ActionItem actionItem, Usuario responsable) {
        Email email = EmailBuilder.startingBlank()
                .from("Reminder Action Item", "votacion-roots@10pines.com")
                .to(responsable.getName(), responsable.getMail())
                .withSubject("Tenes Action-Items pendientes")
                .withPlainText("Recordá hacerte cargo del Action Item: " + actionItem.getDescripcion())
                .buildEmail();
        mailer.sendMail(email);
    }

    private void validarActionItem(ActionItem unActionItem) {
        if(unActionItem.getDescripcion() == null|| unActionItem.getResponsables() == null) {
            throw new RuntimeException(EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    @Override
    public void onSetResponsables(ActionItem actionItem) {
        validarActionItem(actionItem);
        actionItem.getResponsables().stream()
                .filter(responsables -> StringUtils.isNotBlank(responsables.getMail()))
                .forEach(responsable -> sendMail(actionItem, responsable));
    }
}
