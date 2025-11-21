package com.vodadfone.dependentresource;

import com.vodadfone.model.Website;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent
public class WebsiteConfigMapDependent extends CRUDKubernetesDependentResource<ConfigMap, Website> {

    public static final String NAME_SUFFIX = "html";

    public WebsiteConfigMapDependent() {
        super(ConfigMap.class);
    }

    @Override
    protected ConfigMap desired(Website website, Context<Website> context) {
        var meta = website.getMetadata();
        var name = meta.getName() + "-" + NAME_SUFFIX;
        var namespace = meta.getNamespace();

        var message = website.getSpec().getMessage();
        if (message == null || message.isBlank()) {
            message = "Hello from Website '" + meta.getName() + "'";
        }

        String html = """
            <html>
              <head><title>Website Operator</title></head>
              <body>
                <h1>%s</h1>
                <p>Implemented with Java Operator SDK + Quarkus</p>
              </body>
            </html>
            """.formatted(message);

        return new ConfigMapBuilder()
            .withNewMetadata()
            .withName(name)
            .withNamespace(namespace)
            .addToLabels("app.kubernetes.io/managed-by", "website-operator")
            .addToLabels("app.kubernetes.io/name", meta.getName())
            .endMetadata()
            .addToData("index.html", html)
            .build();
    }
}