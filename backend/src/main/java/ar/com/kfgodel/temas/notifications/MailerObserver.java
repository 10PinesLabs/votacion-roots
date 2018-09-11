package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;

import java.util.List;

public abstract class MailerObserver {

    public static MailerObserver create(){
        return new OnNewActionItemObserver(MailerConfiguration.getMailer());
    }

    public abstract void notificar(List<ActionItem> oldActionItem, List<ActionItem> newActionItem);

}
