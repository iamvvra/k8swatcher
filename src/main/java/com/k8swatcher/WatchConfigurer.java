package com.k8swatcher;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;

@ApplicationScoped
public class WatchConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WatchConfigurer.class);

    private KubernetesClient client;
    private WatchConfig watchConfig;
    private ResourceWatchMap resourceWatchMap;
    private NotificationPublisher notificationPublisher;

    @Inject
    private Instance<ResourceWatcher<?>> resourceWatcher;

    @Inject
    private Vertx vertx;

    @Inject
    public WatchConfigurer(KubernetesClient client, WatchConfig watchConfig, ResourceWatchMap resourceWatchMap,
            NotificationPublisher notificationPublisher) {
        this.client = client;
        this.watchConfig = watchConfig;
        this.resourceWatchMap = resourceWatchMap;
        this.notificationPublisher = notificationPublisher;
        // VertxOptions options = new VertxOptions().setMetricsOptions(new
        // MetricsOptions().setEnabled(true));
        // vertx = Vertx.vertx(options);
    }

    public void registerResourceWatch(@Observes StartupEvent _e) {
        Set<String> namespaces = getWatchedNamespaceList();
        log.info("Registering watchers for objects, {}, in namespaces {}", watchConfig.watchedResources(), namespaces);
        namespaces.stream().forEach(ns -> {
            watchConfig.watchedResources().stream().forEach(res -> {
                createWatchVerticle(res, ns);
            });
        });
    }

    private void createWatchVerticle(String res, String namespace) {
        Resource object = Resource.valueOf(res);
        log.debug("watching {} in namespace {}, " + object.name(), namespace);
        switch (object) {
        case ALL:
            break;
        case POD:
            resourceWatchMap.watchPods().accept(client, namespace);
            break;
        case SERVICE:
            resourceWatchMap.watchServices().accept(client, namespace);
            break;
        case PERSISTENTVOLUMECLAIM:
            resourceWatchMap.watchPVCs().accept(client, namespace);
            break;
        case SECRET:
            resourceWatchMap.watchSecrets().accept(client, namespace);
            break;
        case CONFIGMAP:
            resourceWatchMap.watchConfigmaps().accept(client, namespace);
            break;
        case JOB:
            resourceWatchMap.watchJobs().accept(client, namespace);
            break;
        case CRONJOB:
            resourceWatchMap.watchCronJobs().accept(client, namespace);
            break;
        case HORIZONTALPODAUTOSCALER:
            resourceWatchMap.watchHorizontalPodAutoScalers().accept(client, namespace);
            break;
        case BUILD:
            resourceWatchMap.watchBuild().accept(client, namespace);
            break;
        case DEPLOYMENTCONFIG:
            resourceWatchMap.watchDeploymentConfig().accept(client, namespace);
            break;
        case GROUP:
            resourceWatchMap.watchGroup().accept(client, namespace);
            break;
        case IMAGESTREAM:
            resourceWatchMap.watchImageStream().accept(client, namespace);
            break;
        case IMAGESTREAMTAG:
            resourceWatchMap.watchImageStreamTag().accept(client, namespace);
            break;
        case OAUTHCLIENT:
            resourceWatchMap.watchOAuthClient().accept(client, namespace);
            break;
        case PROJECT:
            resourceWatchMap.watchProject().accept(client, namespace);
            break;
        case ROUTE:
            resourceWatchMap.watchRoute().accept(client, namespace);
            break;
        case USER:
            resourceWatchMap.watchUser().accept(client, namespace);
            break;
        case DEPLOYMENT:
            resourceWatchMap.watchDeployments().accept(client, namespace);
            break;
        case STATEFULSET:
            resourceWatchMap.watchStatefulsets().accept(client, namespace);
            break;
        case INGRESS:
            resourceWatchMap.watchIngresses().accept(client, namespace);
            break;
        case NODE:
            resourceWatchMap.watchNodes().accept(client, namespace);
            break;
        case NAMESPACE:
            resourceWatchMap.watchNamespaces().accept(client, namespace);
            break;
        case PERSISTENTVOLUME:
            resourceWatchMap.watchPVs().accept(client, namespace);
            break;
        case DAEMONSET:
            resourceWatchMap.watchDaemonsets().accept(client, namespace);
            break;
        case ROLE:
            resourceWatchMap.watchRoles().accept(client, namespace);
            break;
        case ROLEBINDING:
            resourceWatchMap.watchRoleBindings().accept(client, namespace);
            break;
        case CLUSTERROLE:
            resourceWatchMap.watchClusterRoles().accept(client, namespace);
            break;
        case CLUSTERROLEBINDING:
            resourceWatchMap.watchClusterRoleBindings().accept(client, namespace);
            break;
        case REPLICATIONCONTROLLER:
            resourceWatchMap.watchReplicationControllers().accept(client, namespace);
            break;
        case SERVICEACCOUNT:
            resourceWatchMap.watchServiceAccounts().accept(client, namespace);
            break;
        case RESOURCEQUOTA:
            resourceWatchMap.watchResourceQuotas().accept(client, namespace);
            break;
        case ENDPOINT:
            resourceWatchMap.watchEndpoints().accept(client, namespace);
            break;
        case LIMITRANGE:
            resourceWatchMap.watchLimitRanges().accept(client, namespace);
            break;
        case REPLICASET:
            resourceWatchMap.watchReplicaSets().accept(client, namespace);
            break;
        case EVENT:
            resourceWatchMap.watchEvents().accept(client, namespace);
            break;
        default:
            break;
        }
    }

    private Set<String> getWatchedNamespaceList() {
        Set<String> namespaces = watchConfig.getNamespaces();
        if (watchConfig.watchAllNamespaces()) {
            namespaces = client.namespaces().list().getItems().stream().map(n -> n.getMetadata().getName())
                    .collect(Collectors.toSet());
        }
        return namespaces;
    }

    public void registerEventWatch(@Observes StartupEvent _e) {
        if (watchConfig.watchAllNamespaces()) {
            log.info("Registering event watchers for all namespaces");
            List<String> nses = client.namespaces().list().getItems().stream().map(ns -> ns.getMetadata().getName())
                    .collect(Collectors.toList());
            log.debug("No namespace provided, watching events from all namespaces" + nses);
            resourceWatchMap.watchEvents().accept(client, null);
        } else {
            log.info("Registering event watchers for namespaces - " + watchConfig.getNamespaces());
            watchConfig.getNamespaces().stream().forEach(ns -> {
                resourceWatchMap.watchEvents().accept(client, ns);
            });
        }
    }

    @PostConstruct
    public void init() throws IOException {
        notificationPublisher.sendMessage(watchConfig.startupMessage(), Level.NORMAL);
    }

    public void destroy(@Observes ShutdownEvent _e) throws IOException {
        log.info("k8swatcher shutting down");
        notificationPublisher.sendMessage(watchConfig.shutdownMessage(), Level.WARNING);
        vertx.close();
    }

}