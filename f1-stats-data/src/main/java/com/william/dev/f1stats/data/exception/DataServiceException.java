package com.william.dev.f1stats.data.exception;

public class DataServiceException extends Exception {

    public DataServiceException(final String message, final Throwable error) {
        super(message, error);
    }
}
