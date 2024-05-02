package org.example.userregistr.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendUserEvent(Event event, String id) {
        kafkaTemplate.send("user-event", id, event);
    }
}
