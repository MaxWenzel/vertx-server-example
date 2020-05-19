package org.devzone.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;
import org.devzone.vertx.config.Event;
import org.devzone.vertx.models.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void start() {

        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("vertx_db")
                .setUser("postgres")
                .setPassword("devPassword");

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        // Create the pooled client
        PgPool client = PgPool.pool(vertx, connectOptions, poolOptions);

        vertx.eventBus().<String>consumer(Event.LOCATION_BY_POSTALCODE).handler(msg -> {
            //JsonObject body = msg.body();
            String postalCode = msg.body(); //body.getString("postalCode");
            // Get a connection from the pool
            client.getConnection(ar -> {

                if (ar.succeeded()) {

                    logger.info("Connected");

                    // Obtain our connection
                    SqlConnection conn = ar.result();

                    // All operations execute on the same connection
                    conn
                        .preparedQuery("select p.postalcode , p.locality , p.state from postalcode p where p.postalcode = $1")
                        .execute(Tuple.of(postalCode), dbResult -> {
                            if (dbResult.succeeded()) {
                                List<Address> addresses = new ArrayList<>();
                                dbResult.result().forEach(row ->
                                        addresses.add(new Address(row.getString("postalcode"), row.getString("locality"), row.getString("state")))
                                );
                                logger.info("Found {} addresses", addresses.size());
                                String jsonArray = "[]";
                                try {
                                    jsonArray = mapper.writeValueAsString(addresses);
                                } catch (JsonProcessingException e) {
                                    logger.error("Cannot serialize address list", e);
                                }
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.put("addresses", jsonArray);
                                msg.reply(jsonObject);
                            } else {
                                logger.error("Failure: {}", dbResult.cause().getMessage());
                            }
                            conn.close();
                        });
                } else {
                    logger.error("Could not connect: {}", ar.cause().getMessage());
                }
            });
        });


    }

}
