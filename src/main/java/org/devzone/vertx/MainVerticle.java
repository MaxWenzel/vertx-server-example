package org.devzone.vertx;

import io.vertx.core.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    public static void main(final String[] args) {
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.info("Start the server");

        DeploymentOptions restOpts = new DeploymentOptions()
                .setWorkerPoolSize(30);

        DeploymentOptions workerOpts = new DeploymentOptions()
                .setWorker(true)
                .setWorkerPoolSize(30);

        vertx.deployVerticle(RestVerticle.class.getName(), restOpts);
    }
}
