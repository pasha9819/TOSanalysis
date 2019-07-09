package ru.pasha.tosamara.classifiers.xml.route.full;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "routes")
public class Routes {
    @XmlElement(name = "route")
    private List<Route> list = new ArrayList<>();

    @XmlTransient
    public List<Route> getList() {
        return list;
    }

    public void setList(List<Route> list) {
        this.list = list;
    }
}
