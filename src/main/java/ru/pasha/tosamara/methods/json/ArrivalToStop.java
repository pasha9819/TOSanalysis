package ru.pasha.tosamara.methods.json;

import com.google.gson.annotations.SerializedName;
import ru.pasha.tosamara.classifiers.RouteClassifier;
import ru.pasha.tosamara.classifiers.StopClassifier;
import ru.pasha.tosamara.classifiers.xml.route.full.Route;
import ru.pasha.tosamara.classifiers.xml.stop.Stop;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class ArrivalToStop {
    private Calendar date = new GregorianCalendar();
    private String stateNumber;
    @SerializedName("KR_ID")
    private Integer routeId;
    private Integer hullNo;
    private Integer nextStopId;
    private Double spanLength;
    private Double remainingLength;
    private Double timeInSeconds;
    /**
     * This field doesn't exist in JSON-schema.
     */
    private Stop stop;

    public ArrivalToStop() {
    }

    public ArrivalToStop(Calendar date, String stateNumber, Integer routeId, Integer hullNo,
                         Integer nextStopId, Double spanLength, Double remainingLength,
                         Double timeInSeconds) {
        this.date = date;
        this.stateNumber = stateNumber;
        this.routeId = routeId;
        this.hullNo = hullNo;
        this.nextStopId = nextStopId;
        this.spanLength = spanLength;
        this.remainingLength = remainingLength;
        this.timeInSeconds = timeInSeconds;
        this.stop = StopClassifier.findById(nextStopId);
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber = stateNumber;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getHullNo() {
        return hullNo;
    }

    public void setHullNo(Integer hullNo) {
        this.hullNo = hullNo;
    }

    public Integer getNextStopId() {
        return nextStopId;
    }

    public void setNextStopId(Integer nextStopId) {
        this.nextStopId = nextStopId;
    }

    public Double getSpanLength() {
        return spanLength;
    }

    public void setSpanLength(Double spanLength) {
        this.spanLength = spanLength;
    }

    public Double getRemainingLength() {
        return remainingLength;
    }

    public void setRemainingLength(Double remainingLength) {
        this.remainingLength = remainingLength;
    }

    public Double getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Double timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    private Integer getPrevStopID(){
        Route route = RouteClassifier.findById(routeId);
        if (route == null) {
            return null;
        }
        for (int i = 1; i < route.getStops().size(); i++)
            if (route.getStops().get(i).getKS_ID().equals(nextStopId))
                return route.getStops().get(i - 1).getKS_ID();
        return null;
    }

    public Stop getPrevStop(){
        Integer id = getPrevStopID();
        if (id != null){
            return StopClassifier.findById(id);
        }
        return null;
    }

    public boolean isTransportNearSomeStop(){
        List<Route.Stop> stops = RouteClassifier.findById(routeId).getStops();
        if (Objects.equals(nextStopId, stops.get(stops.size() - 1).getKS_ID())){
            return remainingLength < 70 || spanLength - remainingLength < 30;
        }
        return remainingLength < 50 || spanLength - remainingLength < 30;
    }


}
