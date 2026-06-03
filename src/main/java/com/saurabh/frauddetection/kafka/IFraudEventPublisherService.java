package com.saurabh.frauddetection.kafka;

public interface IFraudEventPublisherService {
    public void publish(FraudDecisionEvent event);
}
