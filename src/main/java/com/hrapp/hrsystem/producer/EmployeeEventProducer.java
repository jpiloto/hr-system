package com.hrapp.hrsystem.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrapp.hrsystem.event.EmployeeCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    public void sendEmployeeCreatedEvent(EmployeeCreatedEvent event) {
        boolean isLocal = Arrays.asList(environment.getActiveProfiles()).contains("local");
        if (isLocal) {
            log.info("Local profile active. Skipping Kafka publish for employee: {}", event.getName());
            return;
        }

        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("employee.created", message);
            log.info("Published employee.created event: {}", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}

