package edu.uoc.expensemanager.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public final class Utils {
    private Utils(){

    }
    public static boolean isEmptyTextView(TextView txt){
        return txt.getText().toString().compareTo("") == 0;
    }
    public static boolean isEmptyString(String txt){
        return txt.compareTo("") == 0;
    }
}

