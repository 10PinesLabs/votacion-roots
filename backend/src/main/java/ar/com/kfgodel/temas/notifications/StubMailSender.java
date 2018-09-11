package ar.com.kfgodel.temas.notifications;

import ar.com.kfgodel.temas.config.HerokuPriorityConfigSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubMailSender extends MailSender {

    public static Logger LOG = LoggerFactory.getLogger(HerokuPriorityConfigSelector.class);

    @Override
    public void sendMail(String userMail, String subject, String descripcion) {
        LOG.info("Stub Mail Sender ejecutandose. Envía mail a: " + userMail + ", con asunto: "
                + subject + "y descripcion: " + descripcion);
    }
}
