package edu.uoc.mapgame.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Quiz implements Parcelable {
    public String description;
    public double lon;
    public double lat;

    public Quiz(String desc, double lon, double lat){
        this.description = desc;
        this.lon = lon;
        this.lat = lat;
    }

    protected Quiz(Parcel in) {
        description = in.readString();
        lon = in.readDouble();
        lat = in.readDouble();
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeDouble(lon);
        parcel.writeDouble(lat);
    }
}
