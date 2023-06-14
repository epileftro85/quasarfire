package com.mercadolibre.quasarfire.dtos;

public class TopSecretResponseDTO {
    private CoordinatesDTO position;
    private String message;

    public CoordinatesDTO getPosition() {
        return position;
    }

    public void setPosition(CoordinatesDTO position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
