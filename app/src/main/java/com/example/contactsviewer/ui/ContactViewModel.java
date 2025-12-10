package com.example.contactsviewer.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contactsviewer.data.DummyRepository;
import com.example.contactsviewer.model.Contact;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    MutableLiveData<List<Contact>> contacts = new MutableLiveData<>();
    private final DummyRepository repository;

    public ContactViewModel(Application application) {
        super(application);
        repository = new DummyRepository(application);
        loadContacts();
    }

    private void loadContacts() {
        contacts.setValue(repository.getContacts());
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}
