package com.example.contactsviewer.util

import android.text.TextWatcher
import android.widget.EditText

fun EditText.attachPhoneNumberFormatter(): TextWatcher {
    val watcher = PhoneNumberFormatter()
    this.addTextChangedListener(watcher)
    return watcher
}

fun EditText.getNormalizedPhoneNumber(): String {
    return this.text.toString().filter { it.isDigit() }.take(10)
}

fun EditText.isValidPhoneNumber(): Boolean {
    return getNormalizedPhoneNumber().length == 10
}