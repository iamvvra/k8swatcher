package com.k8swatcher;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceEventsVerticle extends AbstractVerticle {

    private Vertx vertx;
    private EventBus eventBus;
    private NotificationPublisher notificationPublisher;

    public ResourceEventsVerticle(Vertx vertx, NotificationPublisher notificationPublisher) {
        this.vertx = vertx;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        eventBus = vertx.eventBus();
        log.debug("listening to the address: " + getAddress());
        eventBus.<String>consumer(getAddress(), handler -> {
            log.debug("received message in address: " + handler.address());
            EventMessage newMessage = JsonUtil.toObject(handler.body().toString(), EventMessage.class);
            notify(newMessage);
        });
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        eventBus.close(handler -> {
            if (handler.succeeded())
                stopFuture.succeeded();
        });
        super.stop(stopFuture);
    }

    private void notify(EventMessage newMessage) {
        notificationPublisher.notifyEvent(newMessage);
    }

    private String getAddress() {
        return null;
    }

}