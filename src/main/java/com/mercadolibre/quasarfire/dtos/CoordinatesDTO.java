package com.mercadolibre.quasarfire.dtos;

import java.io.Serializable;

public class CoordinatesDTO implements Serializable {
    double x;
    double y;

    public CoordinatesDTO() {}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
