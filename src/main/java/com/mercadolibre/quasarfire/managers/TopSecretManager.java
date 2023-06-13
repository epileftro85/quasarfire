package com.mercadolibre.quasarfire.managers;

import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.dtos.TopSecretResponseDTO;

import java.util.List;

public class TopSecretManager {
    private static final double[][] SATELLITE_POSITIONS = {
            { -500, -200 },  //  Kenobi
            { 100, -100 },   // Skywalker
            { 500, 100 }     // Sato
    };

    public TopSecretResponseDTO saveMessage(List<SatelliteMessageDTO> messages) {
        TopSecretResponseDTO response = new TopSecretResponseDTO();
        String message = retrieveMessage(messages);
        double[] messageDistances = getDistancesFromMessage(messages);
        double[] coordinates = getLocation(messageDistances);

        response.setMessage(message);
        response.setCoordinates(coordinates);

        return response;
    }

    public String retrieveMessage(List<SatelliteMessageDTO> satelliteData) {
        StringBuilder messageBuilder = new StringBuilder();
        int messageLength = getMessageLength(satelliteData);

        for (int i = 0; i < messageLength; i++) {
            String word = combineWord(satelliteData, i);
            messageBuilder.append(word).append(" ");
        }

        return messageBuilder.toString().trim();
    }

    private int getMessageLength(List<SatelliteMessageDTO> satelliteData) {
        int length = 0;
        for (SatelliteMessageDTO satellite : satelliteData) {
            if (satellite.getMessage() != null && satellite.getMessage().length > length) {
                length = satellite.getMessage().length;
            }
        }

        return length;
    }

    private String combineWord(List<SatelliteMessageDTO> satelliteMessage, int index) {
        StringBuilder word = new StringBuilder();

        for (SatelliteMessageDTO satellite : satelliteMessage) {
            String[] message = satellite.getMessage();
            if (message != null && index < message.length && !message[index].isEmpty()) {
                word.append(message[index]);
            }
        }

        return word.toString();
    }

    public double[] getDistancesFromMessage(List<SatelliteMessageDTO> satelliteMessage) {
        double[] distances = new double[2];
        int i = 0;
        for (SatelliteMessageDTO satellite: satelliteMessage) {
            distances[i] = satellite.getDistance();
            i++;
        }

        return distances;
    }
    public double[] getLocation(double[] distances) {
        double d1 = distances[0];
        double d2 = distances[1];
        double d3 = distances[2];

        return trilaterate(d1, d2, d3);
    }

    private double[] trilaterate(double d1, double d2, double d3) {
        double[] result = new double[2];

        double x1 = SATELLITE_POSITIONS[0][0];
        double y1 = SATELLITE_POSITIONS[0][1];
        double x2 = SATELLITE_POSITIONS[1][0];
        double y2 = SATELLITE_POSITIONS[1][1];
        double x3 = SATELLITE_POSITIONS[2][0];
        double y3 = SATELLITE_POSITIONS[2][1];

        double first = transmitterCoordinates(x1, x2);
        double second = transmitterCoordinates(y1, y2);
        double third = transmitterCoordinates(x2, x3);
        double fourth = transmitterCoordinates(y2, y3);
        double firstMath = Math.pow(d1, 2) - Math.pow(d2, 2) - Math.pow(x1, 2) + Math.pow(x2, 2) - Math.pow(y1, 2) + Math.pow(y2, 2);
        double secondMath = Math.pow(d2, 2) - Math.pow(d3, 2) - Math.pow(x2, 2) + Math.pow(x3, 2) - Math.pow(y2, 2) + Math.pow(y3, 2);

        double x = (firstMath * fourth - secondMath * second) / (fourth * first - second * third);
        double y = (firstMath * third - first * secondMath) / (second * third - first * fourth);

        result[0] = x;
        result[1] = y;

        return result;
    }

    private double transmitterCoordinates(double pos1, double pos2) {
        return 2 * pos2 - 2 * pos1;
    }
}
