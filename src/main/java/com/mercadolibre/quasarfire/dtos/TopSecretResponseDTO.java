package com.mercadolibre.quasarfire.dtos;

import java.io.Serializable;

public class TopSecretResponseDTO implements Serializable {
    private CoordinatesDTO position;
    private String message;

    public TopSecretResponseDTO() {}

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
