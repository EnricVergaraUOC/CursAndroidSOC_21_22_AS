package edu.uoc.expensemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PayerInfo implements Parcelable {
    public String image_url;
    public String name;
    public String email;
    public int amount;

    public PayerInfo(String image, String name, String email, int amount){
        this.image_url = image;
        this.name = name;
        this.email = email;
        this.amount = amount;
    }

    protected PayerInfo(Parcel in) {
        image_url = in.readString();
        name = in.readString();
        email = in.readString();
        amount = in.readInt();
    }

    public static final Creator<PayerInfo> CREATOR = new Creator<PayerInfo>() {
        @Override
        public PayerInfo createFromParcel(Parcel in) {
            return new PayerInfo(in);
        }

        @Override
        public PayerInfo[] newArray(int size) {
            return new PayerInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image_url);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeInt(amount);
    }
}
