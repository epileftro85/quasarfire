package com.mercadolibre.quasarfire.dtos;

public class SatelliteMessageDTO {
    private String name;
    private Double distance;
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
