package com.example.contactsviewer.data;

import android.content.Context;

import com.example.contactsviewer.model.Contact;
import com.example.contactsviewer.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ContactJsonDataSource {

    public final Context context;
    public final String filename = "contacts.json";

    public ContactJsonDataSource(Context context) {
        this.context = context;
    }

    public List<Contact> loadContacts() {
        String json = JsonUtils.loadJSONFromAsset(context, filename);
        if (json == null) return Collections.emptyList();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Contact>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
