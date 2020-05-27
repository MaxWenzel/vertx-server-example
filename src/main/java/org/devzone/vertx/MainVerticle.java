package org.devzone.vertx;

import io.vertx.config.*;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.devzone.vertx.config.ConfigurationKeys;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    public static void main(final String[] args) {
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }

    @Override
    public void start() throws Exception {
        logger.info("Start the server");

        ConfigRetrieverOptions configRetrieverOptions = getConfigRetrieverOptions();
        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);

        // getConfig is called for initial loading
        configRetriever.getConfig(
                ar -> {
                    int instances = Runtime.getRuntime().availableProcessors();
                    DeploymentOptions deploymentOptionsRest =
                            new DeploymentOptions()
                                    .setInstances(instances) // use instances
                                    .setWorkerPoolSize(30)
                                    .setConfig(ar.result());
                    vertx.deployVerticle(RestVerticle.class, deploymentOptionsRest);
                    DeploymentOptions deploymentOptionsDB =
                            new DeploymentOptions()
                                    .setInstances(instances) // use instances
                                    .setWorkerPoolSize(30)
                                    .setConfig(ar.result());
                    vertx.deployVerticle(DatabaseVerticle.class, deploymentOptionsDB);
                });

    }

    private static ConfigRetrieverOptions getConfigRetrieverOptions() {
        JsonObject classpathFileConfiguration = new JsonObject().put("path", "application.json");
        ConfigStoreOptions classpathFile =
                new ConfigStoreOptions()
                        .setType("file")
                        .setConfig(classpathFileConfiguration);

        JsonObject envFileConfiguration = new JsonObject().put("path", "/etc/default/demo");
        ConfigStoreOptions envFile =
                new ConfigStoreOptions()
                        .setType("file")
                        .setFormat("properties")
                        .setConfig(envFileConfiguration)
                        .setOptional(true);

        JsonArray envVarKeys = new JsonArray();
        for (ConfigurationKeys key : ConfigurationKeys.values()) {
            envVarKeys.add(key.name());
        }
        JsonObject envVarConfiguration = new JsonObject().put("keys", envVarKeys);
        ConfigStoreOptions environment = new ConfigStoreOptions()
                .setType("env")
                .setConfig(envVarConfiguration)
                .setOptional(true);

        return new ConfigRetrieverOptions()
                .addStore(classpathFile) // local values : exhaustive list with sane defaults
                .addStore(environment)   // Container / PaaS friendly to override defaults
                //.addStore(envFile)       // external file, IaaS friendly to override defaults and config hot reloading
                .setScanPeriod(5000);
    }


}
