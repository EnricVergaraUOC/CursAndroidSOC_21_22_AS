package edu.uoc.expensemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TripInfo implements Parcelable {
    public String image_url;
    public String date;
    public String description;
    public String tripID;
    public ArrayList<String> users;
    public TripInfo(String image, String date, String description, String tripID, ArrayList users){
        this.image_url = image;
        this.date = date;
        this.description = description;
        this.tripID = tripID;
        this.users = users;
    }

    protected TripInfo(Parcel in) {
        image_url = in.readString();
        date = in.readString();
        description = in.readString();
        tripID = in.readString();
        users = in.createStringArrayList();
    }

    public static final Creator<TripInfo> CREATOR = new Creator<TripInfo>() {
        @Override
        public TripInfo createFromParcel(Parcel in) {
            return new TripInfo(in);
        }

        @Override
        public TripInfo[] newArray(int size) {
            return new TripInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image_url);
        parcel.writeString(date);
        parcel.writeString(description);
        parcel.writeString(tripID);
        parcel.writeStringList(users);
    }
}
