package ru.pasha.tosamara.classifiers.xml.route.simple;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "routes")
public class SimpleRoutes {
    @XmlElement(name = "route")
    private List<SimpleRoute> list = new ArrayList<>();

    @XmlTransient
    public List<SimpleRoute> getList() {
        return list;
    }

    public void setList(List<SimpleRoute> list) {
        this.list = list;
    }

    public SimpleRoute getById(int id){
        for (SimpleRoute route : list){
            if (id == route.getId()){
                return route;
            }
        }
        return null;
    }
}
