package spring.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class AccuracyPK implements Serializable {
    private long hullNo;
    private Date date;
    private Time time;

    @Id
    @Column(name = "hullNo", nullable = false)
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

    @Column(name = "time", nullable = false)
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccuracyPK that = (AccuracyPK) o;
        return hullNo == that.hullNo &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hullNo, date, time);
    }
}
