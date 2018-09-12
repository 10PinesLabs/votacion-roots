package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;
import convention.persistent.Usuario;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OnDeleteResponsablesObserver extends MailerObserver {

    private final MailSender mailSender;

    public OnDeleteResponsablesObserver(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void notificar(List<ActionItem> oldActionItems, List<ActionItem> newActionItems) {
        oldActionItems.forEach(oldActionItem -> {
                    ActionItem nuevoActionItem = newActionItems.stream().filter(newActionItem -> oldActionItem.getId().equals(newActionItem.getId()))
                            .findFirst().get();
                    usuariosEliminados(oldActionItem.getResponsables(), nuevoActionItem.getResponsables()).forEach(
                            usuarioEliminado -> mailSender.sendMail(usuarioEliminado.getMail(), getAsunto(oldActionItem), getDescripcion(oldActionItem)));
                }
        );
    }

    private List<Usuario> usuariosEliminados(List<Usuario> oldResponsables, List<Usuario> newResponsables) {
        return oldResponsables.stream()
                .filter(old ->
                        !newResponsables.stream().map(newResponsable -> newResponsable.getBackofficeId()).collect(Collectors.toList())
                                .contains(old.getBackofficeId())).collect(Collectors.toList());
    }

    private String getDescripcion(ActionItem actionItem) {
        return "Fuiste eliminado del action item: " + actionItem.getDescripcion() +
        ". Para más información entrá en: http://votacion-roots.herokuapp.com/minuta/"
                + actionItem.getTema().getMinuta().getReunion().getId() + "/ver";
    }

    private String getAsunto(ActionItem actionItem) {
        return "Fuiste removido del action item: " + actionItem.getTema().getTema().getTitulo();
    }
}
