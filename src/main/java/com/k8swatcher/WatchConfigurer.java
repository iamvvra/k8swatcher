package com.k8swatcher;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
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
                log.debug("starting watcher for {} in {} " + res, ns);
                watchFn(res).apply(client, ns);
            });
        });
        log.info("All watchers strted");
    }

    private BiFunction<KubernetesClient, String, Watch> watchFn(String res) {
        Resource object = Resource.valueOf(res);
        switch (object) {
        case ALL:
            return null;
        case POD:
            return resourceWatchMap.watchPods();
        case SERVICE:
            return resourceWatchMap.watchServices();
        case PERSISTENTVOLUMECLAIM:
            return resourceWatchMap.watchPVCs();
        case SECRET:
            return resourceWatchMap.watchSecrets();
        case CONFIGMAP:
            return resourceWatchMap.watchConfigmaps();
        case JOB:
            return resourceWatchMap.watchJobs();
        case CRONJOB:
            return resourceWatchMap.watchCronJobs();
        case HORIZONTALPODAUTOSCALER:
            return resourceWatchMap.watchHorizontalPodAutoScalers();
        case BUILD:
            return resourceWatchMap.watchBuilds();
        case BUILDCONFIG:
            return resourceWatchMap.watchBuildConfig();
        case DEPLOYMENTCONFIG:
            return resourceWatchMap.watchDeploymentConfig();
        case IMAGESTREAM:
            return resourceWatchMap.watchImageStream();
        case IMAGESTREAMTAG:
            return resourceWatchMap.watchImageStreamTag();
        case DEPLOYMENT:
            return resourceWatchMap.watchDeployments();
        case STATEFULSET:
            return resourceWatchMap.watchStatefulsets();
        case INGRESS:
            return resourceWatchMap.watchIngresses();
        case NODE:
            return resourceWatchMap.watchNodes();
        case NAMESPACE:
            return resourceWatchMap.watchNamespaces();
        case PERSISTENTVOLUME:
            return resourceWatchMap.watchPVs();
        case DAEMONSET:
            return resourceWatchMap.watchDaemonsets();
        case ROLE:
            return resourceWatchMap.watchRoles();
        case ROLEBINDING:
            return resourceWatchMap.watchRoleBindings();
        case CLUSTERROLE:
            return resourceWatchMap.watchClusterRoles();
        case CLUSTERROLEBINDING:
            return resourceWatchMap.watchClusterRoleBindings();
        case REPLICATIONCONTROLLER:
            return resourceWatchMap.watchReplicationControllers();
        case SERVICEACCOUNT:
            return resourceWatchMap.watchServiceAccounts();
        case RESOURCEQUOTA:
            return resourceWatchMap.watchResourceQuotas();
        case ENDPOINT:
            return resourceWatchMap.watchEndpoints();
        case LIMITRANGE:
            return resourceWatchMap.watchLimitRanges();
        case REPLICASET:
            return resourceWatchMap.watchReplicaSets();
        case EVENT:
            return resourceWatchMap.watchEvents();
        default:
            return null;
        }
    }

    public void registerEventWatch(@Observes StartupEvent _e) {
        if (watchConfig.watchAllNamespaces()) {
            log.info("Registering event watchers for all namespaces");
            List<String> nses = client.namespaces().list().getItems().stream().map(ns -> ns.getMetadata().getName())
                    .collect(Collectors.toList());
            log.debug("No namespace provided, watching events from all namespaces" + nses);
            resourceWatchMap.watchEvents().apply(client, null);
        } else {
            log.info("Registering event watchers for namespaces - " + watchConfig.getNamespaces());
            watchConfig.getNamespaces().stream().forEach(ns -> resourceWatchMap.watchEvents().apply(client, ns));
        }
    }

    @PostConstruct
    public void init() throws IOException {
        String startMsg = String.format(watchConfig.startupMessage(), watchConfig.clusterName());
        notificationPublisher.sendMessage(startMsg, Level.NORMAL);
    }

    public void destroy(@Observes ShutdownEvent _e) throws IOException {
        log.info("k8swatcher shutting down");
        notificationPublisher.sendMessage(String.format(watchConfig.shutdownMessage(), watchConfig.clusterName()),
                Level.WARNING);
    }

}