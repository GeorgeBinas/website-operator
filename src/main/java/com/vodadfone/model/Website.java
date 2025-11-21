package com.vodadfone.model;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("vodafone.com")
@Version("v1")
@Kind("Website")
public class Website extends CustomResource<WebsiteSpec, WebsiteStatus> implements Namespaced {
}