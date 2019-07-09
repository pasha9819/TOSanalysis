package ru.pasha.spring.util;

/**
 * Point for ChartJS data
 *
 * @see ru.pasha.spring.controllers.MainController#getData(Integer, Integer, String, String)
 */
public class DataPoint {
    private double x, y;

    public DataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

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
