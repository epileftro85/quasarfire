package com.mercadolibre.quasarfire.dtos;

public class TopSecretResponseDTO {
    private double[] coordinates;
    private String message;

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
