package com.mercadolibre.quasarfire;

import com.mercadolibre.quasarfire.dtos.CoordinatesDTO;
import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.utils.TopSecretUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TopSecretUtilTests {
    private static final double[][] SATELLITE_POSITIONS = {
            { -500, -200 },  //  Kenobi
            { 100, -100 },   // Skywalker
            { 500, 100 }     // Sato
    };

    private static final TopSecretUtil topSecretUtil = new TopSecretUtil(SATELLITE_POSITIONS);
    @Test
    void testRetrieveEmptyMessage() {
        List<SatelliteMessageDTO> satellites = new ArrayList<>();

        String retrieveMessage = topSecretUtil.retrieveMessage(satellites);
        String expectedMessage = "";

        Assertions.assertEquals(expectedMessage, retrieveMessage);
    }

    @Test
    void testGetLocation() {
        double[] distances = new double[] {100.0, 115.5, 142.7};

        CoordinatesDTO location = topSecretUtil.getLocation(distances);

        Assertions.assertEquals(-487.2859125, location.getX(), 0.001);
        Assertions.assertEquals(1557.014225, location.getY(), 0.001);
    }
}
