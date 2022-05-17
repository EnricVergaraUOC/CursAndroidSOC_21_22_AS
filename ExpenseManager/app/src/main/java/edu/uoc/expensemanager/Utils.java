package edu.uoc.expensemanager;

import android.widget.TextView;

public final class Utils {
    private Utils(){

    }
    public static boolean isEmpty (TextView txt){
        return txt.getText().toString().compareTo("") == 0;
    }
}
