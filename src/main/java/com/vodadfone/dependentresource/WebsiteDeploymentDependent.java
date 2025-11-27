package com.vodadfone.dependentresource;

import com.vodadfone.model.Website;
import com.vodadfone.model.WebsiteSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpecBuilder;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import java.util.HashMap;
import java.util.Map;

// WebsiteDeploymentDependent manages the Deployment resource for each Website.
// It creates a Deployment for nginx pods, using the Website spec for replicas and message.
@KubernetesDependent
public class WebsiteDeploymentDependent extends CRUDKubernetesDependentResource<Deployment, Website> {

    public WebsiteDeploymentDependent() {
        super(Deployment.class);
    }

    @Override
    protected Deployment desired(Website website, Context<Website> context) {
        // Build a Deployment for nginx pods, with labels and config hash for updates.
        var meta = website.getMetadata();
        String name = meta.getName();
        String namespace = meta.getNamespace();
        WebsiteSpec spec = website.getSpec();
        int replicas = spec.getReplicas() != null ? spec.getReplicas() : 1;

        var messageHash = spec.getMessage() != null ? String.valueOf(spec.getMessage().hashCode()) : "0";
        var annotations = new HashMap<String, String>();
        annotations.put("website/config-hash", messageHash);

        var labels = Map.<String, String>of(
            "app", name,
            "app.kubernetes.io/name", name,
            "app.kubernetes.io/managed-by", "website-operator"
        );

        return new DeploymentBuilder()
            .withNewMetadata()
            .withName(name)
            .withNamespace(namespace)
            .addToLabels(labels)
            .endMetadata()
            .withSpec(
                new DeploymentSpecBuilder()
                    .withReplicas(replicas)
                    .withSelector(new LabelSelectorBuilder().addToMatchLabels(labels).build())
                    .withTemplate(
                        new PodTemplateSpecBuilder()
                            .withNewMetadata()
                            .addToLabels(labels)
                            .addToAnnotations(annotations)
                            .endMetadata()
                            .withSpec(
                                new PodSpecBuilder()
                                    .addNewContainer()
                                    .withName("nginx")
                                    .withImage("nginx:1.25")
                                    .addNewPort()
                                    .withContainerPort(80)
                                    .endPort()
                                    .addNewVolumeMount()
                                    .withName("html")
                                    .withMountPath("/usr/share/nginx/html")
                                    .endVolumeMount()
                                    .endContainer()
                                    .addNewVolume()
                                    .withName("html")
                                    .withNewConfigMap()
                                    .withName(name + "-" + WebsiteConfigMapDependent.NAME_SUFFIX)
                                    .endConfigMap()
                                    .endVolume()
                                    .build()
                            ).build()
                    ).build()
            ).build();
    }
}