package com.vodadfone.dependentresource;

import com.vodadfone.model.Website;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

// WebsiteServiceDependent manages the Service resource for each Website.
// It creates a NodePort service to expose the website pods.
@KubernetesDependent
public class WebsiteServiceDependent extends CRUDKubernetesDependentResource<Service, Website> {

    public WebsiteServiceDependent() {
        super(Service.class);
    }

    @Override
    protected Service desired(Website website, Context<Website> context) {
        // Build a Service that selects pods by app label and exposes port 80.
        // NodePort is used for easy access in local clusters (Minikube/Kind).
        var meta = website.getMetadata();
        String name = meta.getName();
        String namespace = meta.getNamespace();

        var selector = java.util.Map.<String, String>of(
            "app", name
        );

        return new ServiceBuilder()
            .withNewMetadata()
            .withName(name)
            .withNamespace(namespace)
            .addToLabels("app.kubernetes.io/managed-by", "website-operator")
            .addToLabels("app.kubernetes.io/name", name)
            .endMetadata()
            .withNewSpec()
            .withSelector(selector)
            .addNewPort()
            .withPort(80)
            .withTargetPort(new IntOrString(80))
            .endPort()
            .withType("NodePort")   // good for live demo on Minikube/Kind
            .endSpec()
            .build();
    }
}
