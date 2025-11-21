package com.vodadfone.reconciler;

import static io.javaoperatorsdk.operator.api.reconciler.Constants.WATCH_ALL_NAMESPACES;

import com.vodadfone.dependentresource.WebsiteConfigMapDependent;
import com.vodadfone.dependentresource.WebsiteDeploymentDependent;
import com.vodadfone.dependentresource.WebsiteServiceDependent;
import com.vodadfone.model.Website;
import com.vodadfone.model.WebsiteStatus;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.javaoperatorsdk.operator.api.config.informer.Informer;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.Workflow;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import org.jboss.logging.Logger;

@ApplicationScoped
@ControllerConfiguration(
    name = "website-controller",
    informer = @Informer(
        namespaces = WATCH_ALL_NAMESPACES
    )
)
@Workflow(dependents = {
    @Dependent(
        name = "configmap",
        type = WebsiteConfigMapDependent.class
    ),
    @Dependent(
        name = "deployment",
        dependsOn = { "configmap" },
        type = WebsiteDeploymentDependent.class
    ),
    @Dependent(
        name = "service",
        dependsOn = { "deployment" },
        type = WebsiteServiceDependent.class
    )
})
public class WebsiteReconciler implements Reconciler<Website> {

    Logger logger = Logger.getLogger(WebsiteReconciler.class);

    @Override
    public UpdateControl<Website> reconcile(Website website, Context<Website> context) {

        // Read Deployment created by WebsiteDeploymentDependent
        int readyReplicas = context
            .getSecondaryResource(Deployment.class)
            .map(Deployment::getStatus)
            .map(DeploymentStatus::getReadyReplicas)
            .orElse(0);

        logger.infof("Website '%s' has %d ready replicas", website.getMetadata().getName(), readyReplicas);

        WebsiteStatus status = Optional.ofNullable(website.getStatus())
                                       .orElseGet(WebsiteStatus::new);

        status.setReadyReplicas(readyReplicas);
        website.setStatus(status);

        return UpdateControl.patchStatus(website);
    }
}