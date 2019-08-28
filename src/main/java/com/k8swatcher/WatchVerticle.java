package com.k8swatcher;

import javax.inject.Inject;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WatchVerticle extends AbstractVerticle {

    private NotificationPublisher notificationPublisher;
    private String type;

    @Inject
    public WatchVerticle(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MessageConsumer<String> consumer = vertx.eventBus().<String>consumer(getAddress(), handler -> {
            EventMessage event = JsonUtil.toObject(handler.body(), EventMessage.class);
            log.debug("received message in {} eventbus, {}", handler.address(), event.eventDetailShort());
            notify(event);
        });
        consumer.completionHandler(completionHandler -> {
            if (completionHandler.succeeded()) {
                log.debug("watcher verticle started, {}", consumer.address());
                startFuture.complete();
            } else {
                log.debug("watcher verticle failed to start the event bus, {}", getAddress());
                startFuture.fail(completionHandler.cause());
            }
        });
    }

    public void setType(String type) {
        this.type = type;
    }

    protected void notify(EventMessage event) {
        log.debug("broadcasting the {}-{} to channels, {}", event.getAction(), event.kind(), event.eventDetailShort());
        log.debug("broadcasting the event to channels");
        notificationPublisher.notifyEvent(event);
    }

    public String getAddress() {
        String resourceVerticle = this.getClass().getSimpleName();
        return resourceVerticle;
    }

}