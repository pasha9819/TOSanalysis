package ru.pasha.tosamara.methods.json;

import com.google.gson.annotations.SerializedName;

public class Transport {
    private String type;
    private String number;
    private String stateNumber;
    @SerializedName("KR_ID")
    private Integer routeId;
    private Integer hullNo;
    private Integer nextStopId;
    private Double latitude;
    private Double longitude;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
