package com.vodadfone.model;


/**
 * WebsiteStatus captures the observed state of a Website resource.
 * The operator updates this class to reflect the actual status in the cluster.
 */
public class WebsiteStatus {

    /**
     * Number of ready nginx replicas
     * Set by the operator to show how many pods are running and ready.
     */
    private Integer readyReplicas = 0;

    public Integer getReadyReplicas() {
        return readyReplicas;
    }

    public void setReadyReplicas(Integer readyReplicas) {
        this.readyReplicas = readyReplicas;
    }
}
