package com.example.contactsviewer.util;

import android.content.Context;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    public static String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try (InputStream is = context.getAssets().open(filename)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
