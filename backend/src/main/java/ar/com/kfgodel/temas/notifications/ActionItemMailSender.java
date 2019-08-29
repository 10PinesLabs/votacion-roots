package ar.com.kfgodel.temas.notifications;

import ar.com.kfgodel.temas.config.environments.Environment;
import convention.persistent.ActionItem;
import convention.persistent.Usuario;
import org.junit.platform.commons.util.StringUtils;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

public class ActionItemMailSender {
    public static final String EMPTY_ITEM_ACTION_EXCEPTION = "El item debe tener descripción y responsables";
    private Mailer mailer;
    private String hostName;

    public ActionItemMailSender() {
        mailer = MailerConfiguration.getMailer();
        hostName =  Environment.toHandle(System.getenv("ENVIROMENT")).getHostName();
    }

    public void sendMail(ActionItem actionItem, Usuario responsable) {
        Email email = EmailBuilder.startingBlank()
                .from("Reminder Action Item", "votacion-roots@10pines.com")
                .to(responsable.getName(), responsable.getMail())
                .withSubject("Tenes Action-Items pendientes del tema " + actionItem.getTema().getTema().getTitulo())
                .withPlainText("Recordá hacerte cargo del Action Item: " + actionItem.getDescripcion() +
                        ". Para más información entrá a: " + this.hostName + "/minuta/"
                        + actionItem.getTema().getMinuta().getReunion().getId() + "/ver")
                .buildEmail();
        mailer.sendMail(email, true);
    }

    private void validarActionItem(ActionItem unActionItem) {
        if (unActionItem.getDescripcion() == null || unActionItem.getResponsables() == null) {
            throw new RuntimeException(EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    public void onSetResponsables(ActionItem actionItem) {
        validarActionItem(actionItem);
        if (!actionItem.getFueNotificado()) {
            actionItem.getResponsables().stream()
                    .filter(responsables -> StringUtils.isNotBlank(responsables.getMail()))
                    .forEach(responsable -> sendMail(actionItem, responsable));
            actionItem.setFueNotificado(true);
        }
    }
}
