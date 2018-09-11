package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;

public abstract class MailerObserver {

    public static MailerObserver create(){
        return new ActionItemMailSender(MailerConfiguration.getMailer());
    }

    public abstract void onSetResponsables(ActionItem actionItem);

}
