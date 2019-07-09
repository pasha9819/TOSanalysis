package ru.pasha.spring.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "accuracy", schema = "accuracy")
@IdClass(AccuracyPK.class)
public class Accuracy {
    private long hullNo;
    private Date date;
    private Time time;
    private long routeId;
    private long stopId;
    private double forecast;
    private double realTime;

    public Accuracy() {
    }

    @Id
    @Column(name = "hull_no", nullable = false)
    public long getHullNo() {
        return hullNo;
    }

    public void setHullNo(long hullNo) {
        this.hullNo = hullNo;
    }

    @Id
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Id
    @Column(name = "time", nullable = false)
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Column(name = "route_id", nullable = false)
    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    @Column(name = "stop_id", nullable = false)
    public long getStopId() {
        return stopId;
    }

    public void setStopId(long stopId) {
        this.stopId = stopId;
    }

    @Column(name = "forecast", nullable = false)
    public double getForecast() {
        return forecast;
    }

    public void setForecast(double forecast) {
        this.forecast = forecast;
    }

    @Column(name = "realtime", nullable = false)
    public double getRealTime() {
        return realTime;
    }

    public void setRealTime(double realTime) {
        this.realTime = realTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accuracy accuracy = (Accuracy) o;
        return hullNo == accuracy.hullNo &&
                routeId == accuracy.routeId &&
                stopId == accuracy.stopId &&
                Double.compare(accuracy.forecast, forecast) == 0 &&
                Double.compare(accuracy.realTime, realTime) == 0 &&
                Objects.equals(time, accuracy.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hullNo, time);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Accuracy{");
        sb.append("hullNo=").append(hullNo);
        sb.append(", date=").append(date);
        sb.append(", time=").append(time);
        sb.append(", routeId=").append(routeId);
        sb.append(", stopId=").append(stopId);
        sb.append(", forecast=").append(forecast);
        sb.append(", realTime=").append(realTime);
        sb.append('}');
        return sb.toString();
    }
}
