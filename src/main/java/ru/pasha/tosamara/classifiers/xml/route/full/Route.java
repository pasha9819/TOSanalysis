package ru.pasha.tosamara.classifiers.xml.route.full;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "route")
public class Route {

    @XmlType(name = "transportType")
    public static class TransportType{

        private static final Integer BUS_ID = 1;
        private static final Integer TRAM_ID = 3;
        private static final Integer TROL_ID = 4;

        public static final TransportType BUS = new TransportType(BUS_ID);
        public static final TransportType TRAM = new TransportType(TRAM_ID);
        public static final TransportType TROL = new TransportType(TROL_ID);

        public TransportType() {
        }

        public TransportType(Integer id) {
            this.id = id;
        }

        private Integer id;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            if (BUS_ID.equals(id))
                return "Автобус";
            if (TRAM_ID.equals(id))
                return "Трамвай";
            if (TROL_ID.equals(id))
                return "Троллейбус";
            return "Неизвестный транспорт";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TransportType){
                TransportType type = (TransportType)obj;
                return type.id.equals(this.id);
            }
            return false;
        }
    }

    @XmlType(name = "stop")
    public static class Stop{
        private Integer KS_ID;

        public Integer getKS_ID() {
            return KS_ID;
        }

        public void setKS_ID(Integer KS_ID) {
            this.KS_ID = KS_ID;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Stop){
                Stop s = (Stop)obj;
                return s.KS_ID.equals(this.KS_ID);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return KS_ID;
        }
    }

    @XmlElement(name = "KR_ID")
    private Integer id;

    private String number;

    private TransportType transportType;

    private Integer affiliationID;

    @XmlElement(name = "stop")
    private List<Stop> stops = new ArrayList<>();

    public Integer getAffiliationID() {
        return affiliationID;
    }

    public void setAffiliationID(Integer affiliationID) {
        this.affiliationID = affiliationID;
    }

    @XmlTransient
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    @XmlTransient
    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @XmlTransient
    public List<Integer> getStopIdList(){
        List<Integer> stopIdList = new ArrayList<>();
        for(Route.Stop s : getStops()){
            stopIdList.add(s.KS_ID);
        }
        return stopIdList;
    }
}
