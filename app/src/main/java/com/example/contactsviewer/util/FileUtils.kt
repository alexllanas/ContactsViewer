package com.example.contactsviewer.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class FileUtils {

    companion object {
        fun saveAvatarImage(context: Context, uri: Uri, contactId: Long): String? {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri)

                val avatarDir = File(context.filesDir, "avatars")
                if (!avatarDir.exists()) {
                    avatarDir.mkdirs()
                }

                val outputFile = File(avatarDir, "${contactId}_${System.currentTimeMillis()}.jpg")

                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(4096)
                var bytes: Int

                inputStream?.let { stream ->
                    while (stream.read(buffer).also { bytes = it } != -1) {
                        outputStream.write(buffer, 0, bytes)
                    }
                }

                inputStream?.close()
                outputStream.close()

                outputFile.absolutePath

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}