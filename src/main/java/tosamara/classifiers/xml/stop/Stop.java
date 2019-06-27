package tosamara.classifiers.xml.stop;

import javax.xml.bind.annotation.*;

@XmlType(name = "stop")
public class Stop {
    private Integer KS_ID;
    private String title;
    private String adjacentStreet;
    private String direction;
    private String busesMunicipal;
    private String trams;
    private String trolleybuses;

    public Stop(Integer KS_ID) {
        this.KS_ID = KS_ID;
    }

    public Stop() {
    }

    public Integer getKS_ID() {
        return KS_ID;
    }

    public void setKS_ID(Integer KS_ID) {
        this.KS_ID = KS_ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdjacentStreet() {
        return adjacentStreet;
    }

    public void setAdjacentStreet(String adjacentStreet) {
        this.adjacentStreet = adjacentStreet;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBusesMunicipal() {
        return busesMunicipal;
    }

    public void setBusesMunicipal(String busesMunicipal) {
        this.busesMunicipal = busesMunicipal;
    }

    public String getTrams() {
        return trams;
    }

    public void setTrams(String trams) {
        this.trams = trams;
    }

    public String getTrolleybuses() {
        return trolleybuses;
    }

    public void setTrolleybuses(String trolleybuses) {
        this.trolleybuses = trolleybuses;
    }
}
