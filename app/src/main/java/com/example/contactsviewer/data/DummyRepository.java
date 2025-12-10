package com.example.contactsviewer.data;

import android.content.Context;

import com.example.contactsviewer.model.Contact;

import java.util.List;

public class DummyRepository {
    private final ContactJsonDataSource dataSource;

    public DummyRepository(Context context) {
        dataSource = new ContactJsonDataSource(context);
    }

    public List<Contact> getContacts() {
        return dataSource.loadContacts();
    }
}
