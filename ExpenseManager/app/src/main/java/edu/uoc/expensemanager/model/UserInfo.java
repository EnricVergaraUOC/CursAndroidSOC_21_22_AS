package edu.uoc.expensemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    public String name;
    public String url_avatar;


    @Override
    public String toString() {
        return name;
    }

    public UserInfo(String name, String url_avatar){
        this.name = name;
        this.url_avatar = url_avatar;
    }

    protected UserInfo(Parcel in) {
        name = in.readString();
        url_avatar = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url_avatar);
    }
}
