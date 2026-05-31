package org.scn1.exception;

// NO stack trace — writableStackTrace = false
public class ResourceNotFoundLightException extends RuntimeException {

    public ResourceNotFoundLightException(String message) {
        super(
                message,  // mensage
                null,     // cause — no concatened cause
                true,     // enableSuppression
                false     // writableStackTrace — don't catch stacktrace
        );
    }
}