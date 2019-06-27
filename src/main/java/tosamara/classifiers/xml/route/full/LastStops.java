package tosamara.classifiers.xml.route.full;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "lastStops")
public class LastStops {
    @XmlElement(name = "stop")
    private List<Route.Stop> stops;

    public LastStops(){
        stops = new ArrayList<>();
    }

    @XmlTransient
    public List<Route.Stop> getStops() {
        return stops;
    }

    public void setStops(List<Route.Stop> stops) {
        this.stops = stops;
    }
}
