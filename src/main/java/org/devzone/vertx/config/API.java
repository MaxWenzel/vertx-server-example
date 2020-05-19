package org.devzone.vertx.config;

public class API {

    public static final String POSTALCODE = "postalCode";

    public static final String LOCALITY_BY_POSTALCODE = "/postalcodes/:" + POSTALCODE;


    public static final String LB_CHECK = "/lb_healthcheck";
    public static final String GREETING = "/greet";
    public static final String GREETING_W = "/greetw";
    public static final String HELLO_API = "/hello";
    public static final String ROOT = "/";
}
