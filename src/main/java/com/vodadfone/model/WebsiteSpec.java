package com.vodadfone.model;

public class WebsiteSpec {

    /**
     * Message rendered on the nginx index.html
     * This is a user-defined string shown on the website homepage.
     */
    private String message;

    /**
     * Number of nginx replicas
     * Controls how many pods will be created for this website.
     */
    private Integer replicas = 1;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }
}
