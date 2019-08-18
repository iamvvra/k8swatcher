package com.k8swatcher;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class Application {

    @ConfigProperty(name = "k8swatcher.k8s.master-url")
    private String k8sMasterUrl;
    @ConfigProperty(name = "k8swatcher.k8s.oauth-token")
    private String k8sOAuthToken;
    @ConfigProperty(name = "k8swatcher.k8s.trust-self-signed-cert")
    private boolean trustSelfSignedCeriticate;
    @ConfigProperty(name = "k8swatcher.k8s.openshift", defaultValue = "false")
    private boolean openShift;
    @ConfigProperty(name = "k8swatcher.k8s.has-proxy", defaultValue = "false")
    private boolean proxy;
    @ConfigProperty(name = "k8swatcher.k8s.http-proxy")
    private String httpProxy;
    @ConfigProperty(name = "k8swatcher.k8s.https-proxy")
    private String httpsProxy;
    @ConfigProperty(name = "k8swatcher.k8s.proxy-username")
    private String proxyUsername;
    @ConfigProperty(name = "k8swatcher.k8s.proxy-password")
    private String proxyPassword;

    public boolean isOpenshift() {
        return openShift;
    }

    @Produces
    public KubernetesClient defaultKubernetesClient() {
        try {
            log.info("Kubernetes client load");
            Config config = config();
            if (config != null)
                return new DefaultKubernetesClient(config);
            else
                return new DefaultKubernetesClient();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error creating Kubernetes client, check if cluster configuration if provided, or is reachable", e);
        }
    }

    private Config config() {
        Config config = null;
        ConfigBuilder configBuilder = null;
        if (k8sMasterUrl != null && k8sOAuthToken != null) {
            configBuilder = createConfigBuilder(configBuilder);
            configBuilder.withMasterUrl(k8sMasterUrl).withNewOauthToken(k8sOAuthToken)
                    .withTrustCerts(trustSelfSignedCeriticate);
        }
        if (proxy) {
            configBuilder = createConfigBuilder(configBuilder);
            configBuilder.withHttpProxy(httpProxy).withHttpsProxy(httpsProxy).withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword).build();
        }
        if (configBuilder != null) {
            config = configBuilder.build();
        }
        return config;
    }

    private ConfigBuilder createConfigBuilder(ConfigBuilder configBuilder) {
        if (configBuilder == null)
            configBuilder = Config.builder();
        return configBuilder;
    }

}