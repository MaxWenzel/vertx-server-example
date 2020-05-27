package org.devzone.vertx.config;

public class API {

    public static final String POSTALCODE = "postalCode";
    public static final String LOCALITY_BY_POSTALCODE = "/postalcodes/:" + POSTALCODE;
    public static final String LB_CHECK = "/lb_healthcheck";
}
