package com.example.contactsviewer.util

import android.text.Editable
import android.text.TextWatcher

class PhoneNumberFormatter : TextWatcher {
    private var isFormatting = false
    private var deleting = false
    private var digitsBeforeChange: String = ""
    private var digitsBeforeCursor = 0

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
        // Track the digits and cursor position so we can detect when only a formatting
        // character was removed (e.g., user backspaced on "-" or space).
        digitsBeforeChange = s?.filter { it.isDigit() }?.take(10)?.toString().orEmpty()
        digitsBeforeCursor = s?.take(start)?.count { it.isDigit() } ?: 0
        deleting = count > after
    }

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting) return
        if (s == null) return

        isFormatting = true
        var digits = s.toString().filter { it.isDigit() }.take(10)

        if (deleting && digits.length == digitsBeforeChange.length && digits.isNotEmpty()) {
            // Deleting a non-digit character; drop the digit just before the cursor so
            // the user can backspace through formatting characters naturally.
            val removalIndex = (digitsBeforeCursor - 1).coerceIn(0, digitsBeforeChange.lastIndex)
            digits = buildString {
                append(digitsBeforeChange.take(removalIndex))
                append(digitsBeforeChange.drop(removalIndex + 1))
            }
        }

        val formatted = buildString {
            for (i in digits.indices) {
                when (i) {
                    0 -> append("(").append(digits[i])
                    2 -> append(digits[i]).append(") ")
                    5 -> append(digits[i]).append("-")
                    else -> append(digits[i])
                }
            }
        }
        s.replace(0, s.length, formatted)
        isFormatting = false
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        // no-op
    }
}
