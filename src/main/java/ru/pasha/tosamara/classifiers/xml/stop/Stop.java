package ru.pasha.tosamara.classifiers.xml.stop;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "stop")
public class Stop {
    @XmlElement(name = "KS_ID")
    private Integer id;
    private String title;
    private String adjacentStreet;
    private String direction;
    private String busesMunicipal;
    private String trams;
    private String trolleybuses;
    private Double latitude;
    private Double longitude;

    @XmlTransient
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
