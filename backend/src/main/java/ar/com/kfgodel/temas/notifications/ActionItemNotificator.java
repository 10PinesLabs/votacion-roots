package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import convention.persistent.Usuario;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

public class ActionItemNotificator {
    public static final String EMPTY_ITEM_ACTION_EXCEPTION = "El item debe tener descripción y responsables";
    private Mailer mailer;

    public ActionItemNotificator(){
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "votacionroots@gmail.com", "votacionroots1.")
                .buildMailer();
    }

    public void notificar(ActionItem actionItem) {
        validarActionItem(actionItem);
        actionItem.getResponsables().forEach(responsable -> sendMail(actionItem, responsable));
    }

    private void sendMail(ActionItem actionItem, Usuario responsable) {
        Email email = EmailBuilder.startingBlank()
                .from("Reminder Action Item", "votacion-roots@10pines.com")
                .to(responsable.getName(), responsable.getMail())
                .withSubject("Tenes Action-Items pendientes")
                .withPlainText("Recordá hacerte cargo del Action Item: " + actionItem.getDescripcion())
                .buildEmail();
        mailer.sendMail(email);
    }

    private void validarActionItem(ActionItem unActionItem) {
        if(unActionItem.getDescripcion() == null|| unActionItem.getResponsables() == null) throw new RuntimeException(EMPTY_ITEM_ACTION_EXCEPTION);
    }
}
