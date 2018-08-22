package ar.com.kfgodel.temas.notifications;

import convention.persistent.ActionItem;

public abstract class ActionItemObserver {

    public abstract void onSetResponsables(ActionItem actionItem);

}
