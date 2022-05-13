package edu.uoc.expensemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    public String name;
    public String email;
    public String url_avatar;
    public int amountPayed;
    public int toPayOrToReceive;


    @Override
    public String toString() {
        return name;
    }

    public UserInfo(String name, String url_avatar, String email){
        this.name = name;
        this.url_avatar = url_avatar;
        this.email = email;
        this.amountPayed = 0;
        this.toPayOrToReceive = 0;
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
