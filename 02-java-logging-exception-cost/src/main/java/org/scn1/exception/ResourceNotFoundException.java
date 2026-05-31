package org.scn1.exception;

// WITH stack trace — Java default
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message); // writableStackTrace = true by default
    }
}