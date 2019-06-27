package database.entity;

import java.util.Date;

public class Movement {
    private Date date;
    private Integer KR_ID;
    private Integer hullNo;
    private Integer nextStopId;
    private Double nextStopDistance;
    private Double prevStopDistance;

    public Date getDate() {
        return date;
    }

    public Movement setDate(Date date) {
        this.date = date;
        return this;
    }

    public Integer getKR_ID() {
        return KR_ID;
    }

    public Movement setKR_ID(Integer KR_ID) {
        this.KR_ID = KR_ID;
        return this;
    }

    public Integer getHullNo() {
        return hullNo;
    }

    public Movement setHullNo(Integer hullNo) {
        this.hullNo = hullNo;
        return this;
    }

    public Double getNextStopDistance() {
        return nextStopDistance;
    }

    public Movement setNextStopDistance(Double nextStopDistance) {
        this.nextStopDistance = nextStopDistance;
        return this;
    }

    public Integer getNextStopId() {
        return nextStopId;
    }

    public Movement setNextStopId(Integer nextStopId) {
        this.nextStopId = nextStopId;
        return this;
    }

    public Double getPrevStopDistance() {
        return prevStopDistance;
    }

    public Movement setPrevStopDistance(Double prevStopDistance) {
        this.prevStopDistance = prevStopDistance;
        return this;
    }


}
