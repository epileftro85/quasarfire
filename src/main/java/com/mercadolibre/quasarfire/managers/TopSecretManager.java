package com.mercadolibre.quasarfire.managers;

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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

@Component
public class TopSecretManager {
    private final TopSecretUtil topSecretUtil;
    private static final String failMessage = "We can't establish message or distances";
    private static final Logger logger = Logger.getLogger(TopSecretManager.class.getName());
    private static final double[][] SATELLITE_POSITIONS = {
            { -500, -200 },  //  Kenobi
            { 100, -100 },   // Skywalker
            { 500, 100 }     // Sato
    };

    @Autowired
    private ShipMessageRepository shipMessageRepository;

    public TopSecretManager() {
        this.topSecretUtil = new TopSecretUtil(SATELLITE_POSITIONS);
    }

    public String processOneSatellite(String satelliteName, SatelliteMessageDTO message) throws ResourceNotFoundException {
        String response = "FAIL";
        try {
            saveOrUpdateRecord(message, satelliteName);
            response = "OK";
        } catch (Exception e) {
            logger.setLevel(Level.FINE);
            logger.addHandler(new LoggerConsoleHandler());
            logger.log(Level.INFO, e.getMessage());

            throw new ResourceNotFoundException(failMessage);
        }

        return response;
    }

    public TopSecretResponseDTO processAllSatellites(List<SatelliteMessageDTO> messages) throws ResourceNotFoundException {
        TopSecretResponseDTO response = new TopSecretResponseDTO();
        try {
            String message = topSecretUtil.retrieveMessage(messages);
            double[] messageDistances = topSecretUtil.getDistancesFromMessage(messages);
            CoordinatesDTO coordinates = topSecretUtil.getLocation(messageDistances);

            response.setMessage(message);
            response.setPosition(coordinates);
        } catch (Exception e) {
            logger.setLevel(Level.FINE);
            logger.addHandler(new LoggerConsoleHandler());
            logger.log(Level.INFO, failMessage);

            throw new ResourceNotFoundException(failMessage);
        }

        return response;
    }

    public TopSecretResponseDTO processDBPosition() {
       Iterable<ShipMessage> dbShipMessages = shipMessageRepository.findAll();
        List<SatelliteMessageDTO> satelliteMessageDTOs = StreamSupport.stream(dbShipMessages.spliterator(), false)
                .map(shipMessage -> {
                    SatelliteMessageDTO satelliteMessageDTO = new SatelliteMessageDTO();
                    satelliteMessageDTO.setName(shipMessage.getShip());
                    satelliteMessageDTO.setDistance(shipMessage.getDistance());
                    satelliteMessageDTO.setMessage(shipMessage.getMessage());
                    return satelliteMessageDTO;
                })
                .toList();
       return processAllSatellites(satelliteMessageDTOs);
    }

    private void saveOrUpdateRecord(SatelliteMessageDTO shipMessage, String satelliteName) {
        try {
            ShipMessage entityMessage;
            Double distance = shipMessage.getDistance();
            String[] sentShipMessage = shipMessage.getMessage();
            Optional<ShipMessage> dbShipMessage = shipMessageRepository.findByShip(satelliteName);
            LocalDateTime currentDateTime = LocalDateTime.now();

            if (dbShipMessage.isEmpty()) {
                entityMessage = new ShipMessage();
                entityMessage.setShip(satelliteName);
                entityMessage.setDistance(distance);
                entityMessage.setMessage(sentShipMessage);
            } else {
                entityMessage = dbShipMessage.get();
                entityMessage.setMessage(sentShipMessage);
                entityMessage.setDistance(distance);
                entityMessage.setUpdatedAt(currentDateTime);
            }

            shipMessageRepository.save(entityMessage);
        } catch (Exception e) {
            logger.setLevel(Level.FINE);
            logger.addHandler(new LoggerConsoleHandler());
            logger.log(Level.INFO, e.getMessage());
        }
    }
}
