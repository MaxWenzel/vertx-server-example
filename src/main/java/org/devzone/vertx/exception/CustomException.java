package org.devzone.vertx.exception;

public class CustomException extends RuntimeException{
    public CustomException(String msg, int status){
        super(msg);
    }
}
