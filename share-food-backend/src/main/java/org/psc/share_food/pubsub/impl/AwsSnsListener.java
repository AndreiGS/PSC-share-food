package org.psc.share_food.pubsub.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import org.jboss.logging.Logger;
import org.psc.share_food.pubsub.SnsListener;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class AwsSnsListener implements SnsListener {

    private static final Logger LOG = Logger.getLogger(AwsSnsListener.class);
    private static final String QUEUE_URL = System.getenv().getOrDefault(
            "USER_EVENTS_QUEUE_URL", "https://sqs.eu-north-1.amazonaws.com/402289717488/user-events-queue");

    private SqsClient sqsClient;
    private ExecutorService executorService;
    private final AtomicBoolean running = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        this.sqsClient = SqsClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(System.getenv().getOrDefault("AWS_REGION", "eu-north-1")))
                .build();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void onStartup(@Observes Startup startup) {
        startListening();
    }

    @Override
    public void startListening() {
        if (running.compareAndSet(false, true)) {
            executorService.submit(this::pollMessages);
            LOG.info("Started listening to SQS queue: " + QUEUE_URL);
        }
    }

    private void pollMessages() {
        while (running.get()) {
            try {
                ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                        .queueUrl(QUEUE_URL)
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(20) // Long polling
                        .build();

                List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

                for (Message message : messages) {
                    try {
                        // Just print the message for now
                        LOG.info("Received message from SQS: " + message.body());

                        // Delete the message after processing
                        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                                .queueUrl(QUEUE_URL)
                                .receiptHandle(message.receiptHandle())
                                .build();
                        sqsClient.deleteMessage(deleteRequest);
                    } catch (Exception e) {
                        LOG.error("Error processing SQS message", e);
                    }
                }
            } catch (Exception e) {
                LOG.error("Error polling SQS messages", e);
                try {
                    Thread.sleep(5000); // Backoff on error
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @Override
    @PreDestroy
    public void shutdown() {
        running.set(false);
        executorService.shutdown();
        sqsClient.close();
    }
}
