package tosamara.classifiers.xml.route.full;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "routes")
public class RouteList {
    @XmlElement(name = "route")
    private List<Route> routes = new ArrayList<>();

    @XmlTransient
    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
