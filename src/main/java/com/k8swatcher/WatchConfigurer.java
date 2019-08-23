package com.k8swatcher;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class WatchConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WatchConfigurer.class);

    private KubernetesClient client;
    private WatchConfig watchConfig;
    private ResourceWatchMap resourceWatchMap;
    private NotificationPublisher notificationPublisher;

    @Inject
    public WatchConfigurer(KubernetesClient client, WatchConfig watchConfig, ResourceWatchMap resourceWatchMap,
            NotificationPublisher notificationPublisher) {
        this.client = client;
        this.watchConfig = watchConfig;
        this.resourceWatchMap = resourceWatchMap;
        this.notificationPublisher = notificationPublisher;
    }

    public void registerResourceWatch(@Observes StartupEvent _e) {
        Set<String> namespaces = watchConfig.getNamespaces();
        log.info("Registering watchers for objects, {}, in namespaces {}", watchConfig.watchedResources(), namespaces);
        if (watchConfig.watchAllNamespaces()) {
            namespaces = client.namespaces().list().getItems().stream().map(n -> n.getMetadata().getName())
                    .collect(Collectors.toSet());
        }
        namespaces.stream().forEach(ns -> {
            watchConfig.watchedResources().stream().forEach(res -> {
                Resource object = Resource.valueOf(res);
                log.debug("register watcher for object, " + object.name());
                switch (object) {
                case ALL:
                    break;
                case POD:
                    resourceWatchMap.watchPods().apply(client, ns);
                    break;
                case SERVICE:
                    resourceWatchMap.watchServices().apply(client, ns);
                    break;
                case PERSISTENTVOLUMECLAIM:
                    resourceWatchMap.watchPVCs().apply(client, ns);
                    break;
                case SECRET:
                    resourceWatchMap.watchSecrets().apply(client, ns);
                    break;
                case CONFIGMAP:
                    resourceWatchMap.watchConfigmaps().apply(client, ns);
                    break;
                case JOB:
                    resourceWatchMap.watchJobs().apply(client, ns);
                    break;
                case CRONJOB:
                    resourceWatchMap.watchCronJobs().apply(client, ns);
                    break;
                case HORIZONTALPODAUTOSCALER:
                    resourceWatchMap.watchHorizontalPodAutoScalers().apply(client, ns);
                    break;
                case BUILD:
                    log.debug("openshift build, TODO");
                    break;
                case DEPLOYMENTCONFIG:
                    log.debug("openshift dc, TODO");
                    break;
                case DEPLOYMENT:
                    resourceWatchMap.watchDeployments().apply(client, ns);
                    break;
                case STATEFULSET:
                    resourceWatchMap.watchStatefulsets().apply(client, ns);
                    break;
                case INGRESS:
                    resourceWatchMap.watchIngresses().apply(client, ns);
                    break;
                case NODE:
                    resourceWatchMap.watchNodes().apply(client, ns);
                    break;
                case NAMESPACE:
                    resourceWatchMap.watchNamespaces().apply(client, ns);
                    break;
                case PERSISTENTVOLUME:
                    resourceWatchMap.watchPVs().apply(client, ns);
                    break;
                case DAEMONSET:
                    resourceWatchMap.watchDaemonsets().apply(client, ns);
                    break;
                case ROLE:
                    resourceWatchMap.watchRoles().apply(client, ns);
                    break;
                case ROLEBINDING:
                    resourceWatchMap.watchRoleBindings().apply(client, ns);
                    break;
                case CLUSTERROLE:
                    resourceWatchMap.watchClusterRoles().apply(client, ns);
                    break;
                case CLUSTERROLEBINDING:
                    resourceWatchMap.watchClusterRoleBindings().apply(client, ns);
                    break;
                case REPLICATIONCONTROLLER:
                    resourceWatchMap.watchReplicationControllers().apply(client, ns);
                    break;
                case SERVICEACCOUNT:
                    resourceWatchMap.watchServiceAccounts().apply(client, ns);
                    break;
                case RESOURCEQUOTA:
                    resourceWatchMap.watchResourceQuotas().apply(client, ns);
                    break;
                case ENDPOINT:
                    resourceWatchMap.watchEndpoints().apply(client, ns);
                    break;
                case LIMITRANGE:
                    resourceWatchMap.watchLimitRanges().apply(client, ns);
                    break;
                case REPLICASET:
                    resourceWatchMap.watchReplicaSets().apply(client, ns);
                    break;
                case EVENT:
                    resourceWatchMap.watchEvents().apply(client, ns);
                    break;
                default:
                    break;
                }
            });
        });
    }

    public void registerEventWatch(@Observes StartupEvent _e) {
        if (watchConfig.watchAllNamespaces()) {
            log.info("Registering event watchers for all namespaces");
            List<String> nses = client.namespaces().list().getItems().stream().map(ns -> ns.getMetadata().getName())
                    .collect(Collectors.toList());
            log.debug("No namespace provided, watching events from all namespaces" + nses);
            resourceWatchMap.watchEvents().apply(client, null);
            // client.events().inAnyNamespace().watch(new EventsWatcher(watchConfig,
            // notificationPublisher));
        } else {
            log.info("Registering event watchers for namespaces - " + watchConfig.getNamespaces());
            watchConfig.getNamespaces().stream().forEach(ns -> resourceWatchMap.watchEvents().apply(client, ns));
        }
    }

    @PostConstruct
    public void init() {
        String startMsg = String.format(watchConfig.startupMessage(), watchConfig.clusterName());
        notificationPublisher.sendMessage(startMsg, Level.NORMAL);
    }

    public void destroy(@Observes ShutdownEvent _e) {
        log.info("k8swatcher shutting down");
        notificationPublisher.sendMessage(String.format(watchConfig.shutdownMessage(), watchConfig.clusterName()),
                Level.WARNING);
    }

}