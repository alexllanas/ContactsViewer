package com.example.contactsviewer.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsviewer.data.ContactRepository
import com.example.contactsviewer.model.Contact
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    val contacts: StateFlow<List<Contact>> = repository.getAllContacts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addContact(context: Context, contact: Contact, selectedImageUri: Uri?) {
        viewModelScope.launch {
            val newContactId = repository.addContact(contact)
            selectedImageUri?.let { uri ->
                saveAvatarImage(context, uri, newContactId)
            }
        }
    }

    fun deleteContact(contactId: Long) {
        viewModelScope.launch {
            repository.deleteContact(contactId)
        }
    }

    fun saveAvatarImage(context: Context, uri: Uri, contactId: Long) {
        viewModelScope.launch {
            repository.saveAvatarImage(context, uri, contactId)
        }
    }
}
