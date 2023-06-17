package com.mercadolibre.quasarfire.managers;

import com.mercadolibre.quasarfire.mappers.SatelliteMessageMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mercadolibre.quasarfire.dtos.CoordinatesDTO;
import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.dtos.TopSecretResponseDTO;
import com.mercadolibre.quasarfire.entities.ShipMessage;
import com.mercadolibre.quasarfire.exceptions.LoggerConsoleHandler;
import com.mercadolibre.quasarfire.exceptions.ResourceNotFoundException;
import com.mercadolibre.quasarfire.repositories.ShipMessageRepository;
import com.mercadolibre.quasarfire.utils.TopSecretUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TopSecretManager {
    @Autowired
    private ShipMessageRepository shipMessageRepository;

    @Autowired
    private SatelliteMessageMapper satelliteMessageMapper;

    @Autowired
    private KafkaTemplate<String, TopSecretResponseDTO> kafkaTemplate;

    private final TopSecretUtil topSecretUtil;

    private static final String failMessage = "We can't establish message or distances";

    private static final Logger logger = Logger.getLogger(TopSecretManager.class.getName());

    private static final double[][] SATELLITE_POSITIONS = {
            { -500, -200 },  //  Kenobi
            { 100, -100 },   // Skywalker
            { 500, 100 }     // Sato
    };

    public TopSecretManager() {
        this.topSecretUtil = new TopSecretUtil(SATELLITE_POSITIONS);
    }


    public TopSecretResponseDTO processAllSatellites(List<SatelliteMessageDTO> messages) throws ResourceNotFoundException {
        TopSecretResponseDTO response = new TopSecretResponseDTO();
        try {
            String message = topSecretUtil.retrieveMessage(messages);
            double[] messageDistances = topSecretUtil.getDistancesFromMessage(messages);
            CoordinatesDTO coordinates = topSecretUtil.getLocation(messageDistances);
            saveOrUpdateRecord(messages);

            response.setMessage(message);
            response.setPosition(coordinates);
            kafkaTemplate.send("topsecrettopic", response);
        } catch (Exception e) {
            logger.setLevel(Level.FINE);
            logger.addHandler(new LoggerConsoleHandler());
            logger.log(Level.INFO, failMessage);

            throw new ResourceNotFoundException(failMessage);
        }

        return response;
    }

    @Cacheable(cacheNames = "topsecretposition")
    public TopSecretResponseDTO processDBPosition() {
       Iterable<ShipMessage> dbShipMessages = shipMessageRepository.findAll();
       List<SatelliteMessageDTO> satelliteMessageDTOs = satelliteMessageMapper.fromDatabaseToList(dbShipMessages);
       return processAllSatellites(satelliteMessageDTOs);
    }


    private void saveOrUpdateRecord(List<SatelliteMessageDTO> shipMessage) {
        try {
            for (SatelliteMessageDTO satelliteMessage : shipMessage) {
                ShipMessage entityMessage = shipMessageRepository.findByShip(satelliteMessage.getName())
                        .orElseGet(ShipMessage::new);

                entityMessage.setShip(satelliteMessage.getName());
                entityMessage.setDistance(satelliteMessage.getDistance());
                entityMessage.setMessage(satelliteMessage.getMessage());
                entityMessage.setUpdatedAt(LocalDateTime.now());

                shipMessageRepository.save(entityMessage);
            }
        } catch (Exception e) {
            logger.setLevel(Level.FINE);
            logger.addHandler(new LoggerConsoleHandler());
            logger.log(Level.INFO, e.getMessage());
        }
    }
}
