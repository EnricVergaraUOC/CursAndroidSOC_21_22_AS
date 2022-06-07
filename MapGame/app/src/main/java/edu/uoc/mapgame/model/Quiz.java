package edu.uoc.mapgame.model;

public class Quiz {
    public String description;
    public double lon;
    public double lat;

    public Quiz(String desc, double lon, double lat){
        this.description = desc;
        this.lon = lon;
        this.lat = lat;
    }
}
