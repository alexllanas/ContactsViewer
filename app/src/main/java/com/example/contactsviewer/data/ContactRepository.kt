package com.example.contactsviewer.data

import android.content.Context
import android.net.Uri
import com.example.contactsviewer.model.Contact
import com.example.contactsviewer.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class ContactRepository(private val database: ContactDatabase) {

    private val contactsFlow = MutableStateFlow<List<Contact>>(emptyList())

    init {
        refreshData()
    }

    fun getAllContacts(): Flow<List<Contact>> = contactsFlow

    suspend fun addContact(contact: Contact) : Long {
        return withContext(Dispatchers.IO) {
            val insertedId = database.insert(contact)
            refreshData()
            insertedId
        }
    }

    fun deleteContact(contactId: Long) {
        database.delete(contactId)
        refreshData()
    }

    fun saveAvatarImage(context: Context, uri: Uri, contactId: Long) {
        val path = FileUtils.saveAvatarImage(context, uri, contactId)
        path?.let {
            database.updateAvatarPath(path, contactId)
            refreshData()
        }
    }

    private fun refreshData() {
        contactsFlow.value = database.getAllContacts()
    }
}