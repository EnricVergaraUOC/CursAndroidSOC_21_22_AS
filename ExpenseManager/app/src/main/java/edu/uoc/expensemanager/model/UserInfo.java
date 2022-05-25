package edu.uoc.expensemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserInfo implements Parcelable{
    public String name;
    public String email;
    public String url_avatar;
    public int amountPayed;
    public int toPayOrToReceive;

    public UserInfo(String email){
        this.email = email;
        this.url_avatar = "";
        this.name = "";
    }

    public UserInfo(String userName, String imgURL, String email){
        this.email = email;
        this.name = userName;
        this.url_avatar = imgURL;
    }

    protected UserInfo(Parcel in) {
        name = in.readString();
        email = in.readString();
        url_avatar = in.readString();
        amountPayed = in.readInt();
        toPayOrToReceive = in.readInt();
    }

    @NonNull
    @Override
    public String toString() {
        if (name != null && name.compareTo("") != 0){
            return name;
        }else{
            return email;
        }
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(url_avatar);
        dest.writeInt(amountPayed);
        dest.writeInt(toPayOrToReceive);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
