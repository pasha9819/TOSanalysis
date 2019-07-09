package ru.pasha.tosamara.classifiers.xml.route.simple;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "route")
public class SimpleRoute {
    private Integer affiliationID;
    @XmlElement(name = "KR_ID")
    private Integer id;

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
}
