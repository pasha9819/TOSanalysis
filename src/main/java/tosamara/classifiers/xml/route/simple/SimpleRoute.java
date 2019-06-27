package tosamara.classifiers.xml.route.simple;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "route")
public class SimpleRoute {
    private Integer affiliationID;
    private Integer KR_ID;

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
}
