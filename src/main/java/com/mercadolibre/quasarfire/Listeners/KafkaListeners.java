package com.mercadolibre.quasarfire.Listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.exceptions.LoggerConsoleHandler;
import com.mercadolibre.quasarfire.managers.TopSecretManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class KafkaListeners {
    private static final Logger logger = Logger.getLogger(KafkaListeners.class.getName());
    @Autowired
    private TopSecretManager topSecretManager;

    private ObjectMapper objectMapper;

    public KafkaListeners(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "topsecrettopic", groupId = "topsecret")
    public void onMessage(String message) {
        try {
            SatelliteMessageDTO satelliteMessageStream = objectMapper.readValue(message, SatelliteMessageDTO.class);

            List<SatelliteMessageDTO> satelliteMessage = new ArrayList<>();
            satelliteMessage.add(satelliteMessageStream);
            topSecretManager.processAllSatellites(satelliteMessage);
        } catch (JsonProcessingException e) {
            logger.setLevel(Level.FINE);
            logger.addHandler(new LoggerConsoleHandler());
            logger.log(Level.INFO, e.getMessage());
        }
    }
}
