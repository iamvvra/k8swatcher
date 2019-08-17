package com.k8swatcher;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class Application {
    @Produces
    public KubernetesClient defaultKubernetesClient() {
        try {
            KubernetesClient client = new DefaultKubernetesClient();
            log.info("kubernetes client loaded, " + client.getMasterUrl() + ", " + client.getVersion());

            return client;
        } catch (Exception e) {
            throw new RuntimeException("Error loading the kube client");
        }
    }

}