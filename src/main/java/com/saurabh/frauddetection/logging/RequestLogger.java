package com.saurabh.frauddetection.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class RequestLogger implements IRequestLogger{

    private static final Logger LOGGER = LoggerFactory.getLogger("REQUEST_LOGGER");

    @Override
    public void info(String message) {
        LOGGER.info(message);
    }

    @Override
    public void info(String transactionId, String message) {
        LOGGER.info("TransactionId: {}, {}", transactionId, message);
    }

    @Override
    public void error(String message, Throwable exception) {
        LOGGER.error(message, exception);
    }

    @Override
    public void error(String transactionId, String message, Throwable exception) {
        LOGGER.error("TransactionId: {}, {}", transactionId, message, exception);
    }
}
