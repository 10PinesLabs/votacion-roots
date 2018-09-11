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
    public void onSetResponsables(ActionItem actionItem) {

    }

    @Override
    public void notificar(List<ActionItem> oldActionItems, List<ActionItem> newActionItems) {
        Stream<ActionItem> actionItemsExistentes = oldActionItems.stream().filter(oldItem -> newActionItems.stream().anyMatch(newItem -> oldItem.getId().equals(newItem.getId())));
        newActionItems.stream().filter(newItem -> !newItem.getResponsables().containsAll(actionItemsExistentes.filter(ai -> ai.getId().equals(newItem.getId())).findFirst().get().getResponsables())).collect(Collectors.toList());
        oldActionItems.forEach(oldActionItem -> {
                    ActionItem nuevoActionItem = newActionItems.stream().filter(newActionItem -> oldActionItem.getId().equals(newActionItem.getId()))
                            .findFirst().get();
                    usuariosEliminados(oldActionItem.getResponsables(), nuevoActionItem.getResponsables()).forEach(
                            usuarioEliminado -> mailSender.sendMail(usuarioEliminado.getMail(), getAsunto(), getDescripcion()));
                }
        );
    }

    private List<Usuario> usuariosEliminados(List<Usuario> oldResponsables, List<Usuario> newResponsables) {
        return oldResponsables.stream()
                .filter(old ->
                        !newResponsables.stream().map(newResponsable -> newResponsable.getBackofficeId()).collect(Collectors.toList())
                                .contains(old.getBackofficeId())).collect(Collectors.toList());
    }

    private String getDescripcion() {
        return "Fuiste eliminado de un action item. Para más información ingresá en: ";
    }

    private String getAsunto() {
        return "Fuiste removido de un action item";
    }
}
