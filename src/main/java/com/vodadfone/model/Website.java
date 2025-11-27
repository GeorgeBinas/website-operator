package com.vodadfone.model;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

// Website represents the custom resource managed by the operator.
// It uses annotations to define its API group, version, and kind for Kubernetes.
// The spec and status types are defined in WebsiteSpec and WebsiteStatus.
@Group("vodafone.com")
@Version("v1")
@Kind("Website")
public class Website extends CustomResource<WebsiteSpec, WebsiteStatus> implements Namespaced {
    // No additional fields; all configuration is in WebsiteSpec and WebsiteStatus.
}