package tosamara.classifiers.xml.route.simple;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "routes")
public class SimpleRouteList {
    @XmlElement(name = "route")
    private List<SimpleRoute> list = new ArrayList<>();

    @XmlTransient
    public List<SimpleRoute> getList() {
        return list;
    }

    public void setList(List<SimpleRoute> list) {
        this.list = list;
    }

    public SimpleRoute getByKR_ID(Integer KR_ID){
        for (SimpleRoute route : list){
            if (KR_ID.equals(route.getKR_ID())){
                return route;
            }
        }
        return null;
    }
}
