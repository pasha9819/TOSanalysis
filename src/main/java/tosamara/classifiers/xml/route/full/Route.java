package tosamara.classifiers.xml.route.full;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "route")
public class Route {

    @XmlType(name = "transportType")
    public static class TransportType{

        private static final Integer BUS = 1;
        private static final Integer TRAM = 3;
        private static final Integer TROL = 4;

        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            if (BUS.equals(id))
                return "Автобус";
            if (TRAM.equals(id))
                return "Трамвай";
            if (TROL.equals(id))
                return "Троллейбус";
            return "Неизвестный транспорт";
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

    private Integer KR_ID;

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

    public Integer getKR_ID() {
        return KR_ID;
    }

    public void setKR_ID(Integer KR_ID) {
        this.KR_ID = KR_ID;
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
}
