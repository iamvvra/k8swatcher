package com.k8swatcher.notifier;

import com.k8swatcher.EventMessage;

public interface Notifier {
    public void sendNotification(EventMessage eventMessage) throws Exception;

    public void sendNotification(String string, Level level) throws Exception;

}