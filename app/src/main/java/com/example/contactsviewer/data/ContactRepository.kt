package com.example.contactsviewer.data

import com.example.contactsviewer.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class ContactRepository(private val database: ContactDao) {

    private val contactsFlow = MutableStateFlow<List<Contact>>(emptyList())

    init {
        contactsFlow.value = database.getAllContacts()
    }

    fun getAllContacts(): Flow<List<Contact>> = contactsFlow

    suspend fun addContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            database.insert(contact)
            contactsFlow.value = database.getAllContacts()
        }
    }

    fun deleteContact(contactId: Long) {
        database.delete(contactId)
        contactsFlow.value = database.getAllContacts()
    }
}