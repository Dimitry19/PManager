package cm.packagemanager.pmanager.common.event;

import cm.packagemanager.pmanager.notification.enums.NotificationType;

public abstract class AEvent<T> {

    protected abstract void generateEvent(NotificationType type) throws Exception;

    protected  void generateEvent(T clazz, String message) throws Exception{}
}
