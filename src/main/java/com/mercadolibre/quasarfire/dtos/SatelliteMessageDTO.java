package com.mercadolibre.quasarfire.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SatelliteMessageDTO {
    private String name;

    private Double distance;
    private String[] message;

    public SatelliteMessageDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public @NotNull Double getDistance() {
        return distance;
    }

    public void setDistance(@NotNull Double distance) {
        this.distance = distance;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }
}
