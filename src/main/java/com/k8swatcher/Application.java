package com.k8swatcher;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@ApplicationScoped
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @ConfigProperty(name = "k8swatcher.k8s.master-url")
    private Optional<String> k8sMasterUrl;
    @ConfigProperty(name = "k8swatcher.k8s.oauth-token")
    private Optional<String> k8sOAuthToken;
    @ConfigProperty(name = "k8swatcher.k8s.trust-self-signed-cert", defaultValue = "true")
    private boolean trustSelfSignedCeriticate;
    @ConfigProperty(name = "k8swatcher.k8s.openshift", defaultValue = "false")
    private boolean openShift;
    @ConfigProperty(name = "k8swatcher.k8s.has-proxy", defaultValue = "false")
    private boolean proxy;
    @ConfigProperty(name = "k8swatcher.k8s.http-proxy")
    private Optional<String> httpProxy;
    @ConfigProperty(name = "k8swatcher.k8s.https-proxy")
    private Optional<String> httpsProxy;
    @ConfigProperty(name = "k8swatcher.k8s.proxy-username")
    private Optional<String> proxyUsername;
    @ConfigProperty(name = "k8swatcher.k8s.proxy-password")
    private Optional<String> proxyPassword;

    public boolean isOpenshift() {
        return openShift;
    }

    @Produces
    @Singleton
    public KubernetesClient defaultKubernetesClient() {
        try {
            DefaultKubernetesClient defaultKubernetesClient = null;
            Config config = config();
            if (config != null) {
                defaultKubernetesClient = new DefaultKubernetesClient(config);
            } else {
                defaultKubernetesClient = new DefaultKubernetesClient();
            }
            log.info("Kubernetes-client connected to {}", defaultKubernetesClient.getMasterUrl());
            return defaultKubernetesClient;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error creating Kubernetes client, check if cluster configuration if provided, or is reachable", e);
        }
    }

    private Config config() {
        Config config = null;
        ConfigBuilder configBuilder = null;
        if (k8sMasterUrl.isPresent() && k8sOAuthToken.isPresent()) {
            configBuilder = createConfigBuilder(configBuilder);
            configBuilder.withMasterUrl(k8sMasterUrl.get()).withNewOauthToken(k8sOAuthToken.get())
                    .withTrustCerts(trustSelfSignedCeriticate);
        }
        if (proxy) {
            configBuilder = createConfigBuilder(configBuilder);
            if (httpProxy.isPresent() && proxyUsername.isPresent() && proxyPassword.isPresent())
                configBuilder.withHttpProxy(httpProxy.get()).withHttpsProxy(httpsProxy.get())
                        .withProxyUsername(proxyUsername.get()).withProxyPassword(proxyPassword.get()).build();
        }
        if (configBuilder != null) {
            config = configBuilder.build();
        }
        return config;
    }

    private ConfigBuilder createConfigBuilder(ConfigBuilder configBuilder) {
        if (configBuilder == null)
            configBuilder = new ConfigBuilder();
        return configBuilder;
    }
}