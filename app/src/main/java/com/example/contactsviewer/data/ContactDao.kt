package com.example.contactsviewer.data

import com.example.contactsviewer.model.Contact

class ContactDao(private val dbHelper: ContactDbHelper) {

    fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ContactDbHelper.TABLE_NAME,
            null, null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(ContactDbHelper.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(ContactDbHelper.COLUMN_NAME))
                val phoneNumber = getString(getColumnIndexOrThrow(ContactDbHelper.COLUMN_PHONE))
                val avatarPath =
                    getString(getColumnIndexOrThrow(ContactDbHelper.COLUMN_AVATAR_PATH))

                contacts.add(
                    Contact(
                        id = id,
                        name = name,
                        phoneNumber = phoneNumber,
                        avatarPath = avatarPath
                    )
                )
            }
        }
        cursor.close()

        return contacts
    }

    fun insert(contact: Contact) {
        val db = dbHelper.readableDatabase

        val insertQuery = """
                INSERT INTO ${ContactDbHelper.TABLE_NAME} 
                (${ContactDbHelper.COLUMN_NAME}, ${ContactDbHelper.COLUMN_PHONE}, ${ContactDbHelper.COLUMN_AVATAR_PATH}) 
                VALUES (?, ?, ?);
            """.trimIndent()
        val stmt = db.compileStatement(insertQuery)
        stmt.bindString(1, contact.name)
        stmt.bindString(2, contact.phoneNumber)
        if (contact.avatarPath != null) {
            stmt.bindString(3, contact.avatarPath)
        } else {
            stmt.bindNull(3)
        }
        stmt.executeInsert()
        stmt.close()
    }

    fun delete(contactId: Long) {
        val db = dbHelper.readableDatabase

        val deleteQuery = """
                DELETE FROM ${ContactDbHelper.TABLE_NAME} 
                WHERE ${ContactDbHelper.COLUMN_ID} = ?;
            """.trimIndent()
        val stmt = db.compileStatement(deleteQuery)
        stmt.bindLong(1, contactId)
        stmt.executeUpdateDelete()
        stmt.close()
    }
}