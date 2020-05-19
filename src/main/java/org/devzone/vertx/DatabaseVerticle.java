package org.devzone.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import org.devzone.vertx.config.Event;
import org.devzone.vertx.models.Address;

import java.nio.charset.StandardCharsets;

public class DatabaseVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.eventBus().<String>consumer(Event.LOCATION_BY_POSTALCODE).handler(msg -> {
            Address address = new Address("76185", "Karlsruhe", "Baden-Württemberg");
            String json = Json.encode(address);
            msg.reply(json);
        });
    }
}
