package com.saurabh.frauddetection.config;

import com.saurabh.frauddetection.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic fraudDecisionsTopic(){
        return TopicBuilder
                .name(KafkaTopics.FRAUD_DECISIONS)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
