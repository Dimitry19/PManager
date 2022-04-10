package cm.travelpost.tp.common.event;

import cm.travelpost.tp.notification.enums.NotificationType;

public abstract class AEvent<T> {

    protected abstract void generateEvent(NotificationType type) throws Exception;

    protected  void generateEvent(T clazz, String message) throws Exception{}
}
