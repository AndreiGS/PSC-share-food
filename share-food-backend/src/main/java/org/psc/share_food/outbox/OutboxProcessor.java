package org.psc.share_food.outbox;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.psc.share_food.pubsub.impl.AwsSnsPublisher;
import org.psc.share_food.dao.impl.OutboxDAOImpl;
import org.psc.share_food.entity.OutboxEvent;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class OutboxProcessor {

    private static final Logger LOG = Logger.getLogger(OutboxProcessor.class);
    private static final int BATCH_SIZE = 10;
    private static final int POLLING_INTERVAL_SECONDS = 5;

    private ScheduledExecutorService scheduler;

    @Inject
    private OutboxDAOImpl outboxDAOImpl;

    @Inject
    private AwsSnsPublisher snsPublisher;

    public void onStartup(@Observes Startup startup) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduler.scheduleWithFixedDelay(
                this::processOutboxEvents,
                POLLING_INTERVAL_SECONDS,
                POLLING_INTERVAL_SECONDS,
                TimeUnit.SECONDS
        );
        LOG.info("Outbox processor scheduled");
    }

    @Transactional
    public void processOutboxEvents() {
        try {
            List<OutboxEvent> events = outboxDAOImpl.findUnprocessedEvents(BATCH_SIZE);

            for (OutboxEvent event : events) {
                try {
                    // Publish to SNS
                    snsPublisher.publishUserEvent(event.getPayload());

                    // Mark as processed
                    event.setProcessedAt(Instant.now());
                    outboxDAOImpl.save(event);

                    LOG.info("Processed outbox event: " + event.getId());
                } catch (Exception e) {
                    LOG.error("Error processing outbox event: " + event.getId(), e);
                    // Don't mark as processed, will be retried in the next batch
                }
            }
        } catch (Exception e) {
            LOG.error("Error in outbox processor", e);
        }
    }

    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
