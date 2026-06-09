package com.saurabh.frauddetection.logging;

public interface IRequestLogger {
    void info(String message);

    void info(String transactionId, String message);

    void error(String message, Throwable exception);

    void error(String transactionId, String message, Throwable exception);

}
