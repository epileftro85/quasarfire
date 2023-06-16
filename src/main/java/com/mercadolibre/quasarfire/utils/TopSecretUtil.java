package com.mercadolibre.quasarfire.utils;

import com.mercadolibre.quasarfire.dtos.CoordinatesDTO;
import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TopSecretUtil {
    private final double[][] satellitePositions;

    public TopSecretUtil(double[][] positions) {
        this.satellitePositions = positions;
    }

    public String retrieveMessage(List<SatelliteMessageDTO> satelliteData) {
        StringBuilder messageBuilder = new StringBuilder();
        int messageLength = getMessageLength(satelliteData);

        for (int i = 0; i < messageLength; i++) {
            String word = combineWordList(satelliteData, i);
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

    public String combineWord(String[] message) {
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < message.length; i ++) {
            messageBuilder.append(message[i]).append(" ");
        }

        return messageBuilder.toString();
    }

    private String combineWordList(List<SatelliteMessageDTO> satelliteMessage, int index) {
        StringBuilder messageBuilder = new StringBuilder();
        Set<String> addedWords = new HashSet<>();

        for (SatelliteMessageDTO satellite : satelliteMessage) {
            String[] message = satellite.getMessage();
            if (message != null && index < message.length && !message[index].isEmpty() && !addedWords.contains(message[index])) {
                messageBuilder.append(message[index]);
                addedWords.add(message[index]);
            }
        }

        return messageBuilder.toString();
    }

    public double[] getDistancesFromMessage(List<SatelliteMessageDTO> satelliteMessage) {
        double[] distances = new double[satelliteMessage.size()];
        int i = 0;
        for (SatelliteMessageDTO satellite: satelliteMessage) {
            distances[i] = satellite.getDistance();
            i++;
        }

        return distances;
    }

    public CoordinatesDTO getLocation(double[] distances) {
        double d1 = distances[0];
        double d2 = distances[1];
        double d3 = distances[2];

        return trilaterate(d1, d2, d3);
    }

    private CoordinatesDTO trilaterate(double d1, double d2, double d3) {
        CoordinatesDTO result = new CoordinatesDTO();

        double x1 = satellitePositions[0][0];
        double y1 = satellitePositions[0][1];
        double x2 = satellitePositions[1][0];
        double y2 = satellitePositions[1][1];
        double x3 = satellitePositions[2][0];
        double y3 = satellitePositions[2][1];

        double first = transmitterCoordinates(x1, x2);
        double second = transmitterCoordinates(y1, y2);
        double third = transmitterCoordinates(x2, x3);
        double fourth = transmitterCoordinates(y2, y3);
        double firstMath = Math.pow(d1, 2) - Math.pow(d2, 2) - Math.pow(x1, 2) + Math.pow(x2, 2) - Math.pow(y1, 2) + Math.pow(y2, 2);
        double secondMath = Math.pow(d2, 2) - Math.pow(d3, 2) - Math.pow(x2, 2) + Math.pow(x3, 2) - Math.pow(y2, 2) + Math.pow(y3, 2);

        double x = (firstMath * fourth - secondMath * second) / (fourth * first - second * third);
        double y = (firstMath * third - first * secondMath) / (second * third - first * fourth);

        result.setX(x);
        result.setY(y);

        return result;
    }

    private double transmitterCoordinates(double pos1, double pos2) {
        return 2 * pos2 - 2 * pos1;
    }
}
