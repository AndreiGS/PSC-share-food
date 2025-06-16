package org.psc.share_food.pubsub.impl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.psc.share_food.pubsub.SnsPublisher;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@ApplicationScoped
public class AwsSnsPublisher implements SnsPublisher {

    private SnsClient snsClient;
    private static final String USER_EVENTS_TOPIC_ARN = System.getenv().getOrDefault(
            "USER_EVENTS_TOPIC_ARN", "arn:aws:sns:eu-north-1:402289717488:user-events");

    @PostConstruct
    public void init() {
        // Use default credentials provider which will use AWS CLI credentials if available
        this.snsClient = SnsClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(System.getenv().getOrDefault("AWS_REGION", "eu-north-1")))
                .build();
    }

    @Override
    public void publishUserEvent(String message) {
        PublishRequest request = PublishRequest.builder()
                .topicArn(USER_EVENTS_TOPIC_ARN)
                .message(message)
                .build();

        snsClient.publish(request);
    }
}
