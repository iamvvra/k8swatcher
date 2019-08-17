package com.k8swatcher;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.k8swatcher.notifier.Notifier;
import com.k8swatcher.notifier.Notifier.Level;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class WatchConfigurer {

    private KubernetesClient client;
    private WatchConfig watchConfig;
    private NotificationPublisher notificationPublisher;

    @Inject
    public WatchConfigurer(KubernetesClient client, WatchConfig watchConfig,
            NotificationPublisher notificationPublisher, Notifier notifier) {
        this.client = client;
        this.watchConfig = watchConfig;
        this.notificationPublisher = notificationPublisher;
    }

    public void registerResourceWatch(@Observes StartupEvent _e) {
        log.info("Registering resource watchers");
        Set<String> namespaces = watchConfig.getNamespaces();
        log.info("watched namespaces: " + namespaces);
        if (watchConfig.watchAllNamespaces()) {
            namespaces = client.namespaces().list().getItems().stream().map(n -> n.getMetadata().getName())
                    .collect(Collectors.toSet());
        }

        namespaces.stream().forEach(ns -> {
            watchConfig.watchedResources().stream().forEach(res -> {
                switch (res) {
                case ALL:
                    break;
                case POD:
                    ResourceWatchMap.watchPods(new ResourceWatcher<>(watchConfig, notificationPublisher)).apply(client,
                            ns);
                    break;
                case SERVICE:
                    ResourceWatchMap.watchServices(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case PERSISTENTVOLUMECLAIM:
                    ResourceWatchMap.watchPVCs(new ResourceWatcher<>(watchConfig, notificationPublisher)).apply(client,
                            ns);
                    break;
                case SECRET:
                    ResourceWatchMap.watchSecrets(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case CONFIGMAP:
                    ResourceWatchMap.watchConfigmaps(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case JOB:
                    ResourceWatchMap.watchJobs(new ResourceWatcher<>(watchConfig, notificationPublisher)).apply(client,
                            ns);
                    break;
                case CRONJOB:
                    ResourceWatchMap.watchCronJobs(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case HORIZONTALPODAUTOSCALER:
                    ResourceWatchMap
                            .watchHorizontalPodAutoScalers(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case BUILD:
                    log.debug("openshift build, TODO");
                    break;
                case DEPLOYMENTCONFIG:
                    log.debug("openshift dc, TODO");
                    break;
                case DEPLOYMENT:
                    ResourceWatchMap.watchDeployments(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case STATEFULSET:
                    ResourceWatchMap.watchStatefulsets(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case INGRESS:
                    ResourceWatchMap.watchIngresses(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case NODE:
                    ResourceWatchMap.watchNodes(new ResourceWatcher<>(watchConfig, notificationPublisher)).apply(client,
                            ns);
                    break;
                case NAMESPACE:
                    ResourceWatchMap.watchNamespaces(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case PERSISTENTVOLUME:
                    ResourceWatchMap.watchPVs(new ResourceWatcher<>(watchConfig, notificationPublisher)).apply(client,
                            ns);
                    break;
                case DAEMONSET:
                    ResourceWatchMap.watchDaemonsets(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case ROLE:
                    ResourceWatchMap.watchRoles(new ResourceWatcher<>(watchConfig, notificationPublisher)).apply(client,
                            ns);
                    break;
                case ROLEBINDING:
                    ResourceWatchMap.watchRoleBindings(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case CLUSTERROLE:
                    ResourceWatchMap.watchClusterRoles(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case CLUSTERROLEBINDING:
                    ResourceWatchMap.watchClusterRoleBindings(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case REPLICATIONCONTROLLER:
                    ResourceWatchMap
                            .watchReplicationControllers(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case SERVICEACCOUNT:
                    ResourceWatchMap.watchServiceAccounts(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case RESOURCEQUOTAS:
                    ResourceWatchMap.watchResourceQuotas(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case ENDPOINTS:
                    ResourceWatchMap.watchEndpoints(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case LIMITRANGE:
                    ResourceWatchMap.watchLimitRanges(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case REPLICASET:
                    ResourceWatchMap.watchReplicaSets(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                case EVENT:
                    ResourceWatchMap.watchEvents(new ResourceWatcher<>(watchConfig, notificationPublisher))
                            .apply(client, ns);
                    break;
                }
            });
        });
    }

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        String startMsg = String.format(watchConfig.startupMessage(), watchConfig.clusterName());
        notificationPublisher.sendMessage(startMsg, Level.NORMAL);
    }

    public void registerEventWatch(@Observes StartupEvent _e) {
        if (watchConfig.watchAllNamespaces()) {
            log.info("Registering event watchers for all namespaces");
            List<String> nses = client.namespaces().list().getItems().stream().map(ns -> ns.getMetadata().getName())
                    .collect(Collectors.toList());
            log.debug("No namespace provided, watching events from all namespaces" + nses);
            client.events().inAnyNamespace().watch(new EventsWatcher(watchConfig, notificationPublisher));
        } else {
            log.info("Registering event watchers for namespaces - " + watchConfig.getNamespaces());
            watchConfig.getNamespaces().stream().forEach(
                    n -> client.events().inNamespace(n).watch(new EventsWatcher(watchConfig, notificationPublisher)));
        }
    }

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        notificationPublisher.sendMessage(String.format(watchConfig.shutdownMessage(), watchConfig.clusterName()),
                Level.WARNING);
    }
}