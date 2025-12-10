package com.example.contactsviewer.model

data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val avatarPath: String? = null
)