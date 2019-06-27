package tosamara.classifiers.xml.stop;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "stops")
public class StopList {
    @XmlElement(name = "stop")
    private List<Stop> stops = new ArrayList<>();

    @XmlTransient
    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}
