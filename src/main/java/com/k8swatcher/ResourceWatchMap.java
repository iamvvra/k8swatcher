package com.k8swatcher;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.LimitRange;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.PersistentVolume;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ResourceQuota;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.api.model.networking.NetworkPolicy;
import io.fabric8.kubernetes.api.model.policy.PodDisruptionBudget;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.Role;
import io.fabric8.kubernetes.api.model.rbac.RoleBinding;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ResourceWatchMap {

    private Instance<ResourceWatcher<?>> resouceWatcher;
    private Vertx vertx;

    @Inject
    public ResourceWatchMap(Instance<ResourceWatcher<?>> resouceWatcher, Vertx vertx) {
        this.resouceWatcher = resouceWatcher;
        this.vertx = vertx;
    }

    public BiConsumer<KubernetesClient, String> watchPods() {
        return (client, namespace) -> {
            PodWatcher podVerticle = resouceWatcher.select(PodWatcher.class).get();
            podVerticle.setType(namespace + "-pod");
            vertx.deployVerticle(podVerticle, handle -> {
                if (handle.succeeded()) {
                    watchPods(podVerticle).apply(client, namespace);
                }
            });
        };
    }

    public BiConsumer<KubernetesClient, String> watchServices() {
        return (client, namespace) -> {
            ServiceWatcher serviceVerticle = resouceWatcher.select(ServiceWatcher.class).get();
            serviceVerticle.setType(namespace + "-service");
            vertx.deployVerticle(serviceVerticle, handle -> {
                if (handle.succeeded()) {
                    watchServices(serviceVerticle).apply(client, namespace);
                }
            });
        };
    }

    <T extends Watcher<Pod>> BiFunction<KubernetesClient, String, Watch> watchPods(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.pods().inNamespace(ns).watch(watcher)
                    : kClient.pods().inAnyNamespace().watch(watcher);
        };
    }

    <T extends Watcher<Service>> BiFunction<KubernetesClient, String, Watch> watchServices(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.services().inNamespace(ns).watch(watcher)
                    : kClient.services().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchPVs() {
        return (t, u) -> {
            PersistentVolumeWatcher verticle = resouceWatcher.select(PersistentVolumeWatcher.class).get();
            verticle.setType(u + "-pv");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchPVs(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<PersistentVolume>> BiFunction<KubernetesClient, String, Watch> watchPVs(T watcher) {
        return (kClient, ns) -> {
            return kClient.persistentVolumes().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchPVCs() {
        return (t, u) -> {
            PersistentVolumeClaimWatcher persistentVolumeClaimWatcher = resouceWatcher
                    .select(PersistentVolumeClaimWatcher.class).get();
            persistentVolumeClaimWatcher.setType(u + "-pvc");
            vertx.deployVerticle(persistentVolumeClaimWatcher, handle -> {
                if (handle.succeeded()) {
                    log.debug("events verticle started-" + u);
                    watchPVCs(persistentVolumeClaimWatcher).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<PersistentVolumeClaim>> BiFunction<KubernetesClient, String, Watch> watchPVCs(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.persistentVolumeClaims().inNamespace(ns).watch(watcher)
                    : kClient.persistentVolumeClaims().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchSecrets() {
        return (t, u) -> {
            SecretWatcher secretWatcher = resouceWatcher.select(SecretWatcher.class).get();
            secretWatcher.setType(u + "-secret");
            vertx.deployVerticle(secretWatcher, handle -> {
                if (handle.succeeded()) {
                    watchSecrets(secretWatcher).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Secret>> BiFunction<KubernetesClient, String, Watch> watchSecrets(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.secrets().inNamespace(ns).watch(watcher)
                    : kClient.secrets().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchConfigmaps() {
        return (t, u) -> {
            ConfigMapWatcher cmWatcher = resouceWatcher.select(ConfigMapWatcher.class).get();
            cmWatcher.setType(u + "-configmap");
            vertx.deployVerticle(cmWatcher, handle -> {
                if (handle.succeeded()) {
                    watchConfigmaps(cmWatcher).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<ConfigMap>> BiFunction<KubernetesClient, String, Watch> watchConfigmaps(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.configMaps().inNamespace(ns).watch(watcher)
                    : kClient.configMaps().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchJobs() {
        return (t, u) -> {
            JobWatcher jobWatcher = resouceWatcher.select(JobWatcher.class).get();
            jobWatcher.setType(u + "-job");
            vertx.deployVerticle(jobWatcher, handle -> {
                if (handle.succeeded()) {
                    watchJobs(jobWatcher).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Job>> BiFunction<KubernetesClient, String, Watch> watchJobs(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.batch().jobs().inNamespace(ns).watch(watcher)
                    : kClient.batch().jobs().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchCronJobs() {
        return (t, u) -> {
            CronJobWatcher cronJobWatcher = resouceWatcher.select(CronJobWatcher.class).get();
            cronJobWatcher.setType(u + "-cronjob");
            vertx.deployVerticle(cronJobWatcher, handle -> {
                if (handle.succeeded()) {
                    watchCronJobs(cronJobWatcher).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<CronJob>> BiFunction<KubernetesClient, String, Watch> watchCronJobs(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.batch().cronjobs().inNamespace(ns).watch(watcher)
                    : kClient.batch().cronjobs().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchNodes() {
        return (t, u) -> {
            NodeWatcher verticle = resouceWatcher.select(NodeWatcher.class).get();
            verticle.setType(u + "-node");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchNodes(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Node>> BiFunction<KubernetesClient, String, Watch> watchNodes(T watcher) {
        return (kClient, ns) -> {
            return kClient.nodes().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchNamespaces() {
        return (t, u) -> {
            NamespaceWatcher verticle = resouceWatcher.select(NamespaceWatcher.class).get();
            verticle.setType(u + "-namespace");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchNamespaces(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Namespace>> BiFunction<KubernetesClient, String, Watch> watchNamespaces(T watcher) {
        return (kClient, ns) -> {
            return kClient.namespaces().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchRoles() {
        return (t, u) -> {
            RoleWatcher verticle = resouceWatcher.select(RoleWatcher.class).get();
            verticle.setType(u + "-role");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchRoles(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Role>> BiFunction<KubernetesClient, String, Watch> watchRoles(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().roles().inNamespace(ns).watch(watcher)
                    : kClient.rbac().roles().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchClusterRoles() {
        return (t, u) -> {
            ClusterRoleWatcher verticle = resouceWatcher.select(ClusterRoleWatcher.class).get();
            verticle.setType(u + "-clusterrole");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchClusterRoles(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<ClusterRole>> BiFunction<KubernetesClient, String, Watch> watchClusterRoles(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().clusterRoles().inNamespace(ns).watch(watcher)
                    : kClient.rbac().clusterRoles().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchRoleBindings() {
        return (t, u) -> {
            RoleBindingWatcher verticle = resouceWatcher.select(RoleBindingWatcher.class).get();
            verticle.setType(u + "-rolebinding");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchRoleBindings(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<RoleBinding>> BiFunction<KubernetesClient, String, Watch> watchRoleBindings(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().roleBindings().inNamespace(ns).watch(watcher)
                    : kClient.rbac().roleBindings().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchClusterRoleBindings() {
        return (t, u) -> {
            ClusterRoleBindingWatcher verticle = resouceWatcher.select(ClusterRoleBindingWatcher.class).get();
            verticle.setType(u + "-clusterrolebinding");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchClusterRoleBindings(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<ClusterRoleBinding>> BiFunction<KubernetesClient, String, Watch> watchClusterRoleBindings(
            T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().clusterRoleBindings().inNamespace(ns).watch(watcher)
                    : kClient.rbac().clusterRoleBindings().inAnyNamespace().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchDeployments() {
        return (t, u) -> {
            DeploymentWatcher verticle = resouceWatcher.select(DeploymentWatcher.class).get();
            verticle.setType(u + "-deployment");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchDeployments(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Deployment>> BiFunction<KubernetesClient, String, Watch> watchDeployments(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().deployments().inNamespace(ns).watch(watcher)
                    : kClient.apps().deployments().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchStatefulsets() {
        return (t, u) -> {
            StatefulSetWatcher verticle = resouceWatcher.select(StatefulSetWatcher.class).get();
            verticle.setType(u + "-statefulset");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchStatefulsets(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<StatefulSet>> BiFunction<KubernetesClient, String, Watch> watchStatefulsets(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().statefulSets().inNamespace(ns).watch(watcher)
                    : kClient.apps().statefulSets().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchDaemonsets() {
        return (t, u) -> {
            DaemonSetWatcher verticle = resouceWatcher.select(DaemonSetWatcher.class).get();
            verticle.setType(u + "-daemonset");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchDaemonsets(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<DaemonSet>> BiFunction<KubernetesClient, String, Watch> watchDaemonsets(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().daemonSets().inNamespace(ns).watch(watcher)
                    : kClient.apps().daemonSets().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchNetworkPolicies() {
        return (t, u) -> {
            NetworkPolicyWatcher verticle = resouceWatcher.select(NetworkPolicyWatcher.class).get();
            verticle.setType(u + "-networkpolicy");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchNetworkPolicies(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<NetworkPolicy>> BiFunction<KubernetesClient, String, Watch> watchNetworkPolicies(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.network().networkPolicies().inNamespace(ns).watch(watcher)
                    : kClient.network().networkPolicies().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchIngresses() {
        return (t, u) -> {
            IngressWatcher verticle = resouceWatcher.select(IngressWatcher.class).get();
            verticle.setType(u + "-ingress");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchIngresses(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Ingress>> BiFunction<KubernetesClient, String, Watch> watchIngresses(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.extensions().ingresses().inNamespace(ns).watch(watcher)
                    : kClient.extensions().ingresses().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchReplicationControllers() {
        return (t, u) -> {
            ReplicationControllerWatcher verticle = resouceWatcher.select(ReplicationControllerWatcher.class).get();
            verticle.setType(u + "-replicationcontroller");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchReplicationControllers(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<ReplicationController>> BiFunction<KubernetesClient, String, Watch> watchReplicationControllers(
            T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.replicationControllers().inNamespace(ns).watch(watcher)
                    : kClient.replicationControllers().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchServiceAccounts() {
        return (t, u) -> {
            ServiceAccountWatcher verticle = resouceWatcher.select(ServiceAccountWatcher.class).get();
            verticle.setType(u + "-sa");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchServiceAccounts(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<ServiceAccount>> BiFunction<KubernetesClient, String, Watch> watchServiceAccounts(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.serviceAccounts().inNamespace(ns).watch(watcher)
                    : kClient.serviceAccounts().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchStorageClasses() {
        return (t, u) -> {
            StorageClassWatcher verticle = resouceWatcher.select(StorageClassWatcher.class).get();
            verticle.setType(u + "-storageclass");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchStorageClasses(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<StorageClass>> BiFunction<KubernetesClient, String, Watch> watchStorageClasses(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.storage().storageClasses().inNamespace(ns).watch(watcher)
                    : kClient.storage().storageClasses().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchResourceQuotas() {
        return (t, u) -> {
            ResourceQuotaWatcher verticle = resouceWatcher.select(ResourceQuotaWatcher.class).get();
            verticle.setType(u + "-resourcequota");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchResourceQuotas(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<ResourceQuota>> BiFunction<KubernetesClient, String, Watch> watchResourceQuotas(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.resourceQuotas().inNamespace(ns).watch(watcher)
                    : kClient.resourceQuotas().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchEndpoints() {
        return (t, u) -> {
            EndpointsWatcher verticle = resouceWatcher.select(EndpointsWatcher.class).get();
            verticle.setType(u + "-endpoints");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchEndpoints(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Endpoints>> BiFunction<KubernetesClient, String, Watch> watchEndpoints(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.endpoints().inNamespace(ns).watch(watcher)
                    : kClient.endpoints().inAnyNamespace().watch(watcher);

        };
    }

    public BiConsumer<KubernetesClient, String> watchHorizontalPodAutoScalers() {
        return (t, u) -> {
            HorizontalPodAutoscalerWatcher horizontalPodAutoscalerWatcher = resouceWatcher
                    .select(HorizontalPodAutoscalerWatcher.class).get();
            horizontalPodAutoscalerWatcher.setType(u + "-hpas");
            vertx.deployVerticle(horizontalPodAutoscalerWatcher, handle -> {
                if (handle.succeeded()) {
                    log.debug("events verticle started-" + u);
                    watchHorizontalPodAutoScalers(horizontalPodAutoscalerWatcher).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<HorizontalPodAutoscaler>> BiFunction<KubernetesClient, String, Watch> watchHorizontalPodAutoScalers(
            T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.autoscaling().horizontalPodAutoscalers().inNamespace(ns).watch(watcher)
                    : kClient.autoscaling().horizontalPodAutoscalers().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchLimitRanges() {
        return (t, u) -> {
            LimitRangeWatcher verticle = resouceWatcher.select(LimitRangeWatcher.class).get();
            verticle.setType(u + "-limitrange");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchLimitRanges(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<LimitRange>> BiFunction<KubernetesClient, String, Watch> watchLimitRanges(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.limitRanges().inNamespace(ns).watch(watcher)
                    : kClient.limitRanges().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchReplicaSets() {
        return (t, u) -> {
            ReplicaSetWatcher verticle = resouceWatcher.select(ReplicaSetWatcher.class).get();
            verticle.setType(u + "-replicaset");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchReplicaSets(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<ReplicaSet>> BiFunction<KubernetesClient, String, Watch> watchReplicaSets(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().replicaSets().inNamespace(ns).watch(watcher)
                    : kClient.apps().replicaSets().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchEvents() {
        return (t, u) -> {
            EventsWatcher serviceVerticle = resouceWatcher.select(EventsWatcher.class).get();
            serviceVerticle.setType(u + "-event");
            vertx.deployVerticle(serviceVerticle, handle -> {
                if (handle.succeeded()) {
                    log.debug("events verticle started-" + u);
                    watchEvents(serviceVerticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<Event>> BiFunction<KubernetesClient, String, Watch> watchEvents(T watcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.events().inNamespace(ns).watch(watcher) : kClient.events().watch(watcher);
        };
    }

    public BiConsumer<KubernetesClient, String> watchDisruptionBudgets() {
        return (t, u) -> {
            DisruptionBudgetWatcher verticle = resouceWatcher.select(DisruptionBudgetWatcher.class).get();
            verticle.setType(u + "-disruptionbudgets");
            vertx.deployVerticle(verticle, handle -> {
                if (handle.succeeded()) {
                    watchDisruptionBudgets(verticle).apply(t, u);
                }
            });
        };
    }

    <T extends Watcher<PodDisruptionBudget>> BiFunction<KubernetesClient, String, Watch> watchDisruptionBudgets(
            T watcher) {
        return (kClient, ns) -> {
            return kClient.policy().podDisruptionBudget().watch(watcher);

        };
    }

}
