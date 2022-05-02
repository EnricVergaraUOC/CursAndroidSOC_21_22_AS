package edu.uoc.expensemanager;

public class TripInfo {
    public String imageB64;
    public String date;
    public String decription;
    public int tripID;

    TripInfo(String image, String date, String description, int tripID){
        this.imageB64 = image;
        this.date = date;
        this.decription = description;
        this.tripID = tripID;
    }

}
