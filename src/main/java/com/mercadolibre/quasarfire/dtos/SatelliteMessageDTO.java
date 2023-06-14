package com.mercadolibre.quasarfire.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SatelliteMessageDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Distance is required")
    private Double distance;
    @NotNull(message = "Message is required")
    @Size(min = 1, message = "Message array must have at least one element")
    private String[] message;

    public SatelliteMessageDTO(String name, Double distance, String[] message) {
        this.name = name;
        this.distance = distance;
        this.message = message;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }
}
