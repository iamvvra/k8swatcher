package com.k8swatcher;

import java.util.Optional;
import java.util.function.BiFunction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.openshift.client.OpenShiftClient;

@ApplicationScoped
public class ResourceWatchMap {

    private WatchConfig config;
    private NotificationPublisher notificationPublisher;

    @Inject
    public ResourceWatchMap(WatchConfig watchConfig, NotificationPublisher notificationPublisher) {
        this.config = watchConfig;
        this.notificationPublisher = notificationPublisher;
    }

    public BiFunction<KubernetesClient, String, Watch> watchPods() {
        return (kClient, ns) -> {
            return ns != null ? kClient.pods().inNamespace(ns).watch(new PodWatcher(config, notificationPublisher))
                    : kClient.pods().inAnyNamespace().watch(new PodWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchServices() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.services().inNamespace(ns).watch(new ServiceWatcher(config, notificationPublisher))
                    : kClient.services().inAnyNamespace().watch(new ServiceWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchPVs() {
        return (kClient, ns) -> {
            return kClient.persistentVolumes().watch(new PersistentVolumeWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchPVCs() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.persistentVolumeClaims().inNamespace(ns)
                            .watch(new PersistentVolumeClaimWatcher(config, notificationPublisher))
                    : kClient.persistentVolumeClaims().inAnyNamespace()
                            .watch(new PersistentVolumeClaimWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchSecrets() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.secrets().inNamespace(ns).watch(new SecretWatcher(config, notificationPublisher))
                    : kClient.secrets().inAnyNamespace().watch(new SecretWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchConfigmaps() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.configMaps().inNamespace(ns).watch(new ConfigMapWatcher(config, notificationPublisher))
                    : kClient.configMaps().inAnyNamespace().watch(new ConfigMapWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchJobs() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.batch().jobs().inNamespace(ns).watch(new JobWatcher(config, notificationPublisher))
                    : kClient.batch().jobs().inAnyNamespace().watch(new JobWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchCronJobs() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.batch().cronjobs().inNamespace(ns)
                            .watch(new CronJobWatcher(config, notificationPublisher))
                    : kClient.batch().cronjobs().inAnyNamespace()
                            .watch(new CronJobWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchNodes() {
        return (kClient, ns) -> {
            return kClient.nodes().watch(new NodeWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchNamespaces() {
        return (kClient, ns) -> {
            return kClient.namespaces().watch(new NamespaceWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchRoles() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.rbac().roles().inNamespace(ns).watch(new RoleWatcher(config, notificationPublisher))
                    : kClient.rbac().roles().inAnyNamespace().watch(new RoleWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchClusterRoles() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.rbac().clusterRoles().inNamespace(ns)
                            .watch(new ClusterRoleWatcher(config, notificationPublisher))
                    : kClient.rbac().clusterRoles().inAnyNamespace()
                            .watch(new ClusterRoleWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchRoleBindings() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.rbac().roleBindings().inNamespace(ns)
                            .watch(new RoleBindingWatcher(config, notificationPublisher))
                    : kClient.rbac().roleBindings().inAnyNamespace()
                            .watch(new RoleBindingWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchClusterRoleBindings() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.rbac().clusterRoleBindings().inNamespace(ns)
                            .watch(new ClusterRoleBindingWatcher(config, notificationPublisher))
                    : kClient.rbac().clusterRoleBindings().inAnyNamespace()
                            .watch(new ClusterRoleBindingWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchDeployments() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.apps().deployments().inNamespace(ns)
                            .watch(new DeploymentWatcher(config, notificationPublisher))
                    : kClient.apps().deployments().inAnyNamespace()
                            .watch(new DeploymentWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchStatefulsets() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.apps().statefulSets().inNamespace(ns)
                            .watch(new StatefulSetWatcher(config, notificationPublisher))
                    : kClient.apps().statefulSets().inAnyNamespace()
                            .watch(new StatefulSetWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchDaemonsets() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.apps().daemonSets().inNamespace(ns)
                            .watch(new DaemonSetWatcher(config, notificationPublisher))
                    : kClient.apps().daemonSets().inAnyNamespace()
                            .watch(new DaemonSetWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchNetworkPolicies() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.network().networkPolicies().inNamespace(ns)
                            .watch(new NetworkPolicyWatcher(config, notificationPublisher))
                    : kClient.network().networkPolicies().inAnyNamespace()
                            .watch(new NetworkPolicyWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchIngresses() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.extensions().ingresses().inNamespace(ns)
                            .watch(new IngressWatcher(config, notificationPublisher))
                    : kClient.extensions().ingresses().inAnyNamespace()
                            .watch(new IngressWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchReplicationControllers() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.replicationControllers().inNamespace(ns)
                            .watch(new ReplicationControllerWatcher(config, notificationPublisher))
                    : kClient.replicationControllers().inAnyNamespace()
                            .watch(new ReplicationControllerWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchServiceAccounts() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.serviceAccounts().inNamespace(ns)
                            .watch(new ServiceAccountWatcher(config, notificationPublisher))
                    : kClient.serviceAccounts().inAnyNamespace()
                            .watch(new ServiceAccountWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchStorageClasses() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.storage().storageClasses().inNamespace(ns)
                            .watch(new StorageClassWatcher(config, notificationPublisher))
                    : kClient.storage().storageClasses().inAnyNamespace()
                            .watch(new StorageClassWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchResourceQuotas() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.resourceQuotas().inNamespace(ns)
                            .watch(new ResourceQuotaWatcher(config, notificationPublisher))
                    : kClient.resourceQuotas().inAnyNamespace()
                            .watch(new ResourceQuotaWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchEndpoints() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.endpoints().inNamespace(ns).watch(new EndpointsWatcher(config, notificationPublisher))
                    : kClient.endpoints().inAnyNamespace().watch(new EndpointsWatcher(config, notificationPublisher));

        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchHorizontalPodAutoScalers() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.autoscaling().horizontalPodAutoscalers().inNamespace(ns)
                            .watch(new HorizontalPodAutoscalerWatcher(config, notificationPublisher))
                    : kClient.autoscaling().horizontalPodAutoscalers()
                            .watch(new HorizontalPodAutoscalerWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchLimitRanges() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.limitRanges().inNamespace(ns).watch(new LimitRangeWatcher(config, notificationPublisher))
                    : kClient.limitRanges().watch(new LimitRangeWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchReplicaSets() {
        return (kClient, ns) -> {
            return ns != null
                    ? kClient.apps().replicaSets().inNamespace(ns)
                            .watch(new ReplicaSetWatcher(config, notificationPublisher))
                    : kClient.apps().replicaSets().watch(new ReplicaSetWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchEvents() {
        return (kClient, ns) -> {
            return ns != null ? kClient.events().inNamespace(ns).watch(new EventsWatcher(config, notificationPublisher))
                    : kClient.events().watch(new EventsWatcher(config, notificationPublisher));
        };
    }

    public BiFunction<KubernetesClient, String, Watch> watchDisruptionBudgets() {
        return (kClient, ns) -> kClient.policy().podDisruptionBudget()
                .watch(new DisruptionBudgetWatcher(config, notificationPublisher));
    }

    public BiFunction<KubernetesClient, String, Watch> watchBuilds() {
        return (osClient, ns) -> Optional.of(ns)
                .map(n -> osClient.adapt(OpenShiftClient.class).builds().inNamespace(n)
                        .watch(new BuildWatcher(config, notificationPublisher)))
                .orElse(osClient.adapt(OpenShiftClient.class).builds().inAnyNamespace()
                        .watch(new BuildWatcher(config, notificationPublisher)));
    }

    public BiFunction<KubernetesClient, String, Watch> watchBuildConfig() {
        return (osClient, ns) -> Optional.of(ns)
                .map(n -> osClient.adapt(OpenShiftClient.class).buildConfigs().inNamespace(n)
                        .watch(new BuildConfigWatcher(config, notificationPublisher)))
                .orElse(osClient.adapt(OpenShiftClient.class).buildConfigs().inAnyNamespace()
                        .watch(new BuildConfigWatcher(config, notificationPublisher)));
    }

    public BiFunction<KubernetesClient, String, Watch> watchImageStream() {
        return (osClient, ns) -> Optional.of(ns)
                .map(n -> osClient.adapt(OpenShiftClient.class).imageStreams().inNamespace(n)
                        .watch(new ImageStreamWatcher(config, notificationPublisher)))
                .orElse(osClient.adapt(OpenShiftClient.class).imageStreams().inAnyNamespace()
                        .watch(new ImageStreamWatcher(config, notificationPublisher)));
    }

    public BiFunction<KubernetesClient, String, Watch> watchImageStreamTag() {
        return (osClient, ns) -> Optional.of(ns)
                .map(n -> osClient.adapt(OpenShiftClient.class).imageStreamTags().inNamespace(n)
                        .watch(new ImageStreamTagWatcher(config, notificationPublisher)))
                .orElse(osClient.adapt(OpenShiftClient.class).imageStreamTags().inAnyNamespace()
                        .watch(new ImageStreamTagWatcher(config, notificationPublisher)));
    }

    public BiFunction<KubernetesClient, String, Watch> watchDeploymentConfig() {
        return (osClient, ns) -> Optional.of(ns)
                .map(n -> osClient.adapt(OpenShiftClient.class).deploymentConfigs().inNamespace(n)
                        .watch(new DeploymentConfigWatcher(config, notificationPublisher)))
                .orElse(osClient.adapt(OpenShiftClient.class).deploymentConfigs().inAnyNamespace()
                        .watch(new DeploymentConfigWatcher(config, notificationPublisher)));
    }
}
