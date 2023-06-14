package com.mercadolibre.quasarfire;

import com.mercadolibre.quasarfire.dtos.CoordinatesDTO;
import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.managers.TopSecretManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TopSecretManagerTests {
    @Test
    void testRetrieveMessage() throws HandleExeption {
        List<SatelliteMessageDTO> satellites = new ArrayList<>();
        satellites.add(new SatelliteMessageDTO("kenobi", 100.0, new String[]{"this", "", "", "", "message"}));
        satellites.add(new SatelliteMessageDTO("skywalker", 115.5, new String[]{"", "is", "", "secret", ""}));
        satellites.add(new SatelliteMessageDTO("sato", 142.7, new String[]{"", "", "a", "", ""}));

        TopSecretManager topSecretManager = new TopSecretManager();

        String retrieveMessage = topSecretManager.retrieveMessage(satellites);
        String expectedMessage = "this is a secret message";

        Assertions.assertEquals(expectedMessage, retrieveMessage);
    }

    @Test
    void testGetLocation() {
        double[] distances = new double[] {100.0, 115.5, 142.7};

        TopSecretManager topSecretManager = new TopSecretManager();

        CoordinatesDTO location = topSecretManager.getLocation(distances);

        Assertions.assertEquals(-487.2859125, location.getX(), 0.001);
        Assertions.assertEquals(1557.014225, location.getY(), 0.001);
    }
}
