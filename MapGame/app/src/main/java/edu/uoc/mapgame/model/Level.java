package edu.uoc.mapgame.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Level implements Parcelable {
    public String name;
    public boolean unlocked;
    public ArrayList<Quiz> quizzes;

    public Level (String name, boolean unlocked){
        this.name = name;
        this.unlocked = unlocked;
        quizzes = new ArrayList<Quiz>();
    }

    protected Level(Parcel in) {
        name = in.readString();
        unlocked = in.readByte() != 0;
        quizzes = in.createTypedArrayList(Quiz.CREATOR);
    }

    public static final Creator<Level> CREATOR = new Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel in) {
            return new Level(in);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeByte((byte) (unlocked ? 1 : 0));
        parcel.writeTypedList(quizzes);
    }
}
