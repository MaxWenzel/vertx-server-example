package org.devzone.vertx.models;

public class Address {

    private final String postalCode;
    private final String locality;
    private final String state;

    public Address(String postalCode, String locality, String state) {
        this.postalCode = postalCode;
        this.locality = locality;
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public String getState() {
        return state;
    }
}
