package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import org.junit.platform.commons.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OnNewActionItemObserver extends MailerObserver {
    private final MailSender mailSender;

    public OnNewActionItemObserver(MailSender unMailSender){
        this.mailSender = unMailSender;
    }

    @Override
    public void notificar(List<ActionItem> oldActionItem, List<ActionItem> newActionItem) {
        newActionItem.stream()
                .filter(newItem -> !oldActionItem.stream().anyMatch(old -> old.equals(newItem)))
                .collect(Collectors.toList())
                .forEach(newItem -> notificarAResponsables(newItem));
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
