package org.devzone.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.devzone.vertx.config.API;
import org.devzone.vertx.web.GlobalHandlers;

import java.util.HashSet;
import java.util.Set;

public class RestVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.info("Start Rest-Verticle");

        Router mainRouter = Router.router(vertx);
        mainRouter.route().consumes("application/json");
        mainRouter.route().produces("application/json");

        Set<String> allowHeaders = getAllowedHeaders();
        Set<HttpMethod> allowMethods = getAllowedMethods();
        mainRouter.route().handler(BodyHandler.create());
        mainRouter.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        //mainRouter.mountSubRouter(API.HELLO_API, helloRouter);
        mainRouter.get(API.LB_CHECK).handler(GlobalHandlers::lbCheck);
        mainRouter.route().failureHandler(GlobalHandlers::error);

        // Create the http server and pass it the router
        vertx.createHttpServer()
                .requestHandler(mainRouter::handle).listen(8080);

    }

    private Set<String> getAllowedHeaders(){
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        return allowHeaders;
    }

    private Set<HttpMethod> getAllowedMethods(){
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        return allowMethods;
    }
}