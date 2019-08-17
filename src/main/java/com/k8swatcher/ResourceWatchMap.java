package com.k8swatcher;

import java.util.function.BiFunction;

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

public final class ResourceWatchMap {

    public final static BiFunction<KubernetesClient, String, Watch> watchPods(ResourceWatcher<Pod> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.pods().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.pods().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchServices(
            ResourceWatcher<Service> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.services().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.services().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchPVs(
            ResourceWatcher<PersistentVolume> ResourceWatcher) {
        return (kClient, ns) -> {
            return kClient.persistentVolumes().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchPVCs(
            ResourceWatcher<PersistentVolumeClaim> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.persistentVolumeClaims().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.persistentVolumeClaims().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchSecrets(
            ResourceWatcher<Secret> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.secrets().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.secrets().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchConfigmaps(
            ResourceWatcher<ConfigMap> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.configMaps().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.configMaps().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchJobs(ResourceWatcher<Job> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.batch().jobs().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.batch().jobs().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchCronJobs(
            ResourceWatcher<CronJob> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.batch().cronjobs().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.batch().cronjobs().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchNodes(ResourceWatcher<Node> ResourceWatcher) {
        return (kClient, ns) -> {
            return kClient.nodes().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchNamespaces(
            ResourceWatcher<Namespace> ResourceWatcher) {
        return (kClient, ns) -> {
            return kClient.namespaces().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchRoles(ResourceWatcher<Role> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().roles().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.rbac().roles().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchClusterRoles(
            ResourceWatcher<ClusterRole> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().clusterRoles().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.rbac().clusterRoles().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchRoleBindings(
            ResourceWatcher<RoleBinding> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().roleBindings().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.rbac().roleBindings().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchClusterRoleBindings(
            ResourceWatcher<ClusterRoleBinding> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.rbac().clusterRoleBindings().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.rbac().clusterRoleBindings().inAnyNamespace().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchDeployments(
            ResourceWatcher<Deployment> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().deployments().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.apps().deployments().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchStatefulsets(
            ResourceWatcher<StatefulSet> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().statefulSets().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.apps().statefulSets().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchDaemonsets(
            ResourceWatcher<DaemonSet> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().daemonSets().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.apps().daemonSets().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchNetworkPolicies(
            ResourceWatcher<NetworkPolicy> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.network().networkPolicies().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.network().networkPolicies().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchIngresses(
            ResourceWatcher<Ingress> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.extensions().ingresses().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.extensions().ingresses().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchReplicationControllers(
            ResourceWatcher<ReplicationController> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.replicationControllers().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.replicationControllers().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchServiceAccounts(
            ResourceWatcher<ServiceAccount> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.serviceAccounts().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.serviceAccounts().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchStorageClasses(
            ResourceWatcher<StorageClass> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.storage().storageClasses().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.storage().storageClasses().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchResourceQuotas(
            ResourceWatcher<ResourceQuota> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.resourceQuotas().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.resourceQuotas().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchEndpoints(
            ResourceWatcher<Endpoints> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.endpoints().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.endpoints().inAnyNamespace().watch(ResourceWatcher);

        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchHorizontalPodAutoScalers(
            ResourceWatcher<HorizontalPodAutoscaler> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.autoscaling().horizontalPodAutoscalers().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.autoscaling().horizontalPodAutoscalers().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchLimitRanges(
            ResourceWatcher<LimitRange> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.limitRanges().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.limitRanges().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchReplicaSets(
            ResourceWatcher<ReplicaSet> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.apps().replicaSets().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.apps().replicaSets().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchEvents(
            ResourceWatcher<Event> ResourceWatcher) {
        return (kClient, ns) -> {
            return ns != null ? kClient.events().inNamespace(ns).watch(ResourceWatcher)
                    : kClient.events().watch(ResourceWatcher);
        };
    }

    public final static BiFunction<KubernetesClient, String, Watch> watchDisruptionBudgets(
            ResourceWatcher<PodDisruptionBudget> ResourceWatcher) {
        return (kClient, ns) -> {
            return kClient.policy().podDisruptionBudget().watch(ResourceWatcher);

        };
    }
}
