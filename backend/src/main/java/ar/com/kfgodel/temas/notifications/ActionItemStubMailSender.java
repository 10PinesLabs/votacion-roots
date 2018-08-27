package ar.com.kfgodel.temas.notifications;

import ar.com.kfgodel.temas.config.HerokuPriorityConfigSelector;
import convention.persistent.ActionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionItemStubMailSender extends MailerObserver {

    public static Logger LOG = LoggerFactory.getLogger(HerokuPriorityConfigSelector.class);

    @Override
    public void onSetResponsables(ActionItem actionItem) {
        LOG.info("Se ejecuta StubMailSender. No envía mails");
    }
}
