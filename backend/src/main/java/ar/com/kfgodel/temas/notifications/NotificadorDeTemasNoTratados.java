package ar.com.kfgodel.temas.notifications;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.google.inject.Inject;
import convention.persistent.Minuta;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import convention.services.MinutaService;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

public class NotificadorDeTemasNoTratados {

    private static final String SENDER_ADDRESS = MailerConfiguration.getSenderAdress();
    private static final String SENDER_NAME = "Aviso Tema No Propuesto";

    @Inject
    private MinutaService minutaService;
    private Mailer mailer;

    public NotificadorDeTemasNoTratados() {
    }

    public static NotificadorDeTemasNoTratados create(DependencyInjector unInjector, Mailer unMailer) {
        NotificadorDeTemasNoTratados notificador = unInjector.createInjected(NotificadorDeTemasNoTratados.class);
        notificador.setMailer(unMailer);
        return notificador;
    }

    public void run() {
        Minuta ultimaMinuta = minutaService.getUltimaMinuta();

        ultimaMinuta.getTemas().stream().filter(temaDeMinuta -> !temaDeMinuta.getFueTratado())
                .forEach(temaDeMinuta -> {
                    TemaDeReunion temaDeReunion = temaDeMinuta.getTema();
                    Usuario autorDelTemaDeReunion = temaDeReunion.getAutor();
                    Email email = EmailBuilder.startingBlank()
                    .from(SENDER_NAME, SENDER_ADDRESS)
                            .to(autorDelTemaDeReunion.getName(), autorDelTemaDeReunion.getMail())
                    .withSubject(getSubjectFor(temaDeReunion))
                    .withPlainText(getMessageFor(temaDeReunion))
                            .buildEmail();
                    mailer.sendMail(email, true);
                });
    }

    private void setMailer(Mailer unMailer) {
        mailer = unMailer;
    }

    private String getSubjectFor(TemaDeReunion temaDeReunion) {
        return "Tu tema \"" + temaDeReunion.getTitulo() + "\" no fue tratado";
    }

    private String getMessageFor(TemaDeReunion temaDeReunion) {
        return "Hola! El tema " +
                "\"" +
                temaDeReunion.getTitulo() +
                "\"" +
                " que presentaste en la roots pasada no fue tratado. Sentite libre de volver a proponerlo!";
    }
}
