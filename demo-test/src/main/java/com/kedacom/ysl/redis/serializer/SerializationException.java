package com.kedacom.ysl.redis.serializer;

public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 3005328936761843993L;

    public SerializationException(String message) {
	super(message);
    }

    public SerializationException(String message, Throwable cause) {
	super(message, cause);
    }

}
