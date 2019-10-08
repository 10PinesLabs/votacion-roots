package ar.com.kfgodel.temas.helpers;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;

import java.util.ArrayList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.doAnswer;

public class MailerMock {

    private List<Email> emailsEnviados = new ArrayList<>();
    private Mailer mailer;

    public MailerMock() {
        mailer = PowerMockito.mock(Mailer.class);
        doAnswer(invocation -> {
            Email email = invocation.getArgument(0);
            emailsEnviados.add(email);
            return null;
        }).when(mailer).sendMail(Mockito.any(Email.class));
    }

    public Mailer getMailer() {
        return mailer;
    }

    public List<Email> getEmailsEnviados() {
        return emailsEnviados;
    }

    public Integer cantidadDeEmailsEnviados() {
        return getEmailsEnviados().size();
    }
}
