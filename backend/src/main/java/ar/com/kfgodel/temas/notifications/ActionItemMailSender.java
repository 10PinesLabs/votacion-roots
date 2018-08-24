package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import convention.persistent.Usuario;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class ActionItemMailSender extends ActionItemObserver{
    public static final String EMPTY_ITEM_ACTION_EXCEPTION = "El item debe tener descripción y responsables";
    private Mailer mailer;

    public ActionItemMailSender(){
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, System.getenv("MAIL"), System.getenv("PSW"))
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
        if(unActionItem.getDescripcion() == null|| unActionItem.getResponsables() == null) throw new RuntimeException(EMPTY_ITEM_ACTION_EXCEPTION);
    }

    @Override
    public void onSetResponsables(ActionItem actionItem) {
        validarActionItem(actionItem);
        actionItem.getResponsables().stream()
                .filter(responsables -> !(responsables.getMail()== null ||
                                            responsables.getMail().isEmpty()))
                .forEach(responsable -> sendMail(actionItem, responsable));
    }
}
