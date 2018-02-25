package com.example.xyzreader.remote;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static final URL BASE_URL;
    private static String TAG = Config.class.toString();

    static {
        URL url = null;
        try {
            url = new URL("https://gist.githubusercontent.com/brainbreaker/f01251b36ed49449487256c7eab3022c/raw/86f4dfc6ce578919ba143cf713e1359009b5534c/hilticommerce.json" );
            System.out.print("Reading URL");
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
            Log.e(TAG, "Please check your internet connection.");
        }

        BASE_URL = url;
    }
}
