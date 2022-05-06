package edu.uoc.expensemanager.model;

public class TripInfo {
    public String image_url;
    public String date;
    public String decription;
    public int tripID;

    public TripInfo(String image, String date, String description, int tripID){
        this.image_url = image;
        this.date = date;
        this.decription = description;
        this.tripID = tripID;
    }

}
