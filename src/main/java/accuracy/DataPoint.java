package accuracy;

import java.util.ArrayList;
import java.util.List;

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

    public static class ListWrapper{
        private List<DataPoint> points;

        public ListWrapper() {
            points = new ArrayList<>();
        }

        public List<DataPoint> getPoints() {
            return points;
        }

        public void setPoints(List<DataPoint> points) {
            this.points = points;
        }
    }

}
