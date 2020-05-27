package org.devzone.vertx.web;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.devzone.vertx.exception.CustomException;

public class GlobalHandlers {
    private GlobalHandlers(){}

    public static void lbCheck(RoutingContext ctx){
        ctx.response().end("ok");
    }

    public static void error(RoutingContext ctx){
        int status;
        String msg;

        Throwable failure = ctx.failure();

        if(CustomException.class.isAssignableFrom(failure.getClass())){
            msg = failure.getMessage();
            status = HttpResponseStatus.BAD_REQUEST.code();
        }
        else {
            System.out.println(failure);
            msg = "Sorry, something went wrong";
            status = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
        }

        // Log the error, and send a json encoded response.
        JsonObject res = new JsonObject().put("status", status).put("message", msg);
        ctx.response().setStatusCode(status).end(res.encode());
    }
}
