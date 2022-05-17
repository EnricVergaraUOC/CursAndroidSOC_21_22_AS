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
    public static boolean isEmpty (TextView txt){
        return txt.getText().toString().compareTo("") == 0;
    }
}

