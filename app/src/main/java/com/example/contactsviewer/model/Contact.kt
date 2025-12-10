package com.example.contactsviewer.model

data class Contact(
    val id: Long? = null,
    val name: String,
    val phoneNumber: String,
    val avatarPath: String? = null
)