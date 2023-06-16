package com.mercadolibre.quasarfire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.quasarfire.dtos.SatelliteDataDTO;
import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.dtos.TopSecretResponseDTO;
import com.mercadolibre.quasarfire.entities.ShipMessage;
import com.mercadolibre.quasarfire.managers.TopSecretManager;
import com.mercadolibre.quasarfire.repositories.ShipMessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TopSecretManagerTest {
    @Autowired
    private TopSecretManager topSecretManager;
    private final String[] message = {"este", "", "", "mensaje", ""};
    private final String shipName = "kenobi";
    private final Double distance = 102.0;
    @Autowired
    private ShipMessageRepository shipMessageRepository;

    @Test
    void testProcessAllSatellites() throws JsonProcessingException {
        String arrayMessage = "{\"satellites\":[{\"name\":\"kenobi\",\"distance\":100,\"message\":[\"este\",\"\",\"\",\"mensaje\",\"\"]},{\"name\":\"skywalker\",\"distance\":115.5,\"message\":[\"\",\"es\",\"\",\"\",\"secreto\"]},{\"name\":\"sato\",\"distance\":142.7,\"message\":[\"este\",\"\",\"un\",\"\",\"\"]}]}";
        ObjectMapper objectMapper = new ObjectMapper();
        SatelliteDataDTO satelliteData = objectMapper.readValue(arrayMessage, SatelliteDataDTO.class);
        List<SatelliteMessageDTO> satelliteMessage = satelliteData.getSatellites();
        TopSecretResponseDTO processAllSatellites = topSecretManager.processAllSatellites(satelliteMessage);
        Assertions.assertEquals(-487.2859125, processAllSatellites.getPosition().getX(), 0.0);
        Assertions.assertEquals(1557.014225, processAllSatellites.getPosition().getY(), 0.0);
        Assertions.assertEquals("este es un mensaje secreto", processAllSatellites.getMessage());
    }

    @Test
    void testProcessDBPosition() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ShipMessage shipMessage = new ShipMessage();
        shipMessage.setShip(shipName);
        shipMessage.setDistance(distance);
        shipMessage.setMessage(message);
        shipMessage.setCreatedAt(localDateTime);
        shipMessageRepository.save(shipMessage);

        TopSecretResponseDTO dbPosition = topSecretManager.processDBPosition();
        Assertions.assertEquals(-487.2859125, dbPosition.getPosition().getX());
        Assertions.assertEquals(1557.014225, dbPosition.getPosition().getY());
        Assertions.assertEquals("este es un mensaje secreto", dbPosition.getMessage());
    }
}
