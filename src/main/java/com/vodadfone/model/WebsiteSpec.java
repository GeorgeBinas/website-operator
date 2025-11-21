package com.vodadfone.model;

public class WebsiteSpec {

    /**
     * Message rendered on the nginx index.html
     */
    private String message;

    /**
     * Number of nginx replicas
     */
    private Integer replicas = 1;

    // getters & setters

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
