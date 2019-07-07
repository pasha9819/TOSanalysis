package tosamara.classifiers.xml.stop;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "stops")
public class Stops {
    @XmlElement(name = "stop")
    private List<Stop> list = new ArrayList<>();

    @XmlTransient
    public List<Stop> getList() {
        return list;
    }

    public void setList(List<Stop> list) {
        this.list = list;
    }
}
