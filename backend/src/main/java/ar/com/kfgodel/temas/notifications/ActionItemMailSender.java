package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import org.junit.platform.commons.util.StringUtils;

import java.util.List;

public class ActionItemMailSender extends MailerObserver {
    public static final String EMPTY_ITEM_ACTION_EXCEPTION = "El item debe tener descripción y responsables";
    private final MailSender mailSender;

    public ActionItemMailSender(MailSender unMailSender){
        this.mailSender = unMailSender;
    }

    private void validarActionItem(ActionItem unActionItem) {
        if (unActionItem.getDescripcion() == null || unActionItem.getResponsables() == null) {
            throw new RuntimeException(EMPTY_ITEM_ACTION_EXCEPTION);
        }
    }

    @Override
    public void onSetResponsables(ActionItem actionItem) {
        validarActionItem(actionItem);
        if (!actionItem.getFueNotificado()) {
            notificarAResponsables(actionItem);
            actionItem.setFueNotificado(true);
        }
    }

    @Override
    public void notificar(List<ActionItem> oldActionItem, List<ActionItem> newActionItem) {

    }

    private void notificarAResponsables(ActionItem actionItem) {
        actionItem.getResponsables().stream()
                .filter(responsables -> StringUtils.isNotBlank(responsables.getMail()))
                .forEach(responsable -> mailSender.sendMail(responsable.getMail(), getAsunto(actionItem), getDescripcion(actionItem)));
    }

    public String getAsunto(ActionItem actionItem) {
        return "Tenes Action-Items pendientes del tema " + actionItem.getTema().getTema().getTitulo();
    }

    public String getDescripcion(ActionItem actionItem) {
        return "Recordá hacerte cargo del Action Item: " + actionItem.getDescripcion() +
                ". Para más información entrá en: http://votacion-roots.herokuapp.com/minuta/"
                + actionItem.getTema().getMinuta().getReunion().getId() + "/ver";
    }
}
