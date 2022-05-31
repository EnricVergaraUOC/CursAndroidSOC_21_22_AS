package edu.uoc.expensemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ExpenseInfo implements Parcelable  {
    public String description;
    public String date;
    public int totalAmount;
    public ArrayList<PayerInfo> payers;
    public String expenseID;

    public ExpenseInfo(String expenseID,
                       String description,
                       String date,
                       int amount,
                       ArrayList<PayerInfo> payers){
        this.expenseID = expenseID;
        this.date = date;
        this.description = description;
        this.totalAmount = amount;
        this.payers = payers;
    }

    protected ExpenseInfo(Parcel in) {
        description = in.readString();
        date = in.readString();
        totalAmount = in.readInt();
        payers = in.createTypedArrayList(PayerInfo.CREATOR);
        expenseID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(date);
        dest.writeInt(totalAmount);
        dest.writeTypedList(payers);
        dest.writeString(expenseID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExpenseInfo> CREATOR = new Creator<ExpenseInfo>() {
        @Override
        public ExpenseInfo createFromParcel(Parcel in) {
            return new ExpenseInfo(in);
        }

        @Override
        public ExpenseInfo[] newArray(int size) {
            return new ExpenseInfo[size];
        }
    };
}
