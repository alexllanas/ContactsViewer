package com.example.contactsviewer.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.contactsviewer.databinding.DialogAddContactBinding
import com.example.contactsviewer.model.Contact
import com.example.contactsviewer.util.attachPhoneNumberFormatter
import com.example.contactsviewer.util.getNormalizedPhoneNumber
import com.example.contactsviewer.util.isValidPhoneNumber

class AddContactDialog(
    private val listener: Listener
) : DialogFragment() {

    private var _binding: DialogAddContactBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddContactBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add New Contact")
            .setView(binding.root)
            .create()

        binding.editTextPhone.attachPhoneNumberFormatter()

        binding.buttonAdd.setOnClickListener {
            val name = binding.editTextName.text.toString()

            var valid = true
            if (name.isEmpty()) {
                binding.editTextName.error = "Name cannot be empty"
                valid = false
            }
            if (!binding.editTextPhone.isValidPhoneNumber()) {
                binding.editTextPhone.error = "Invalid phone number"
                valid = false
            }
            if (!valid) return@setOnClickListener

            val contact = Contact(
                name = binding.editTextName.text.toString(),
                phoneNumber = binding.editTextPhone.text.toString()
            )
            listener.onAdd(contact, selectedImageUri)
            dismiss()
        }

        binding.buttonCancel.setOnClickListener { dismiss() }

        binding.imageAvatar.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).apply { type = "image/*" }
            galleryLauncher.launch(intent)
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

            val imageUri = result.data?.data ?: return@registerForActivityResult
            val currentBinding = _binding ?: return@registerForActivityResult
            Glide.with(requireContext())
                .load(imageUri)
                .circleCrop()
                .into(currentBinding.imageAvatar)

            selectedImageUri = imageUri
        }

    interface Listener {
        fun onAdd(contact: Contact, selectedImageUri: Uri? = null)
        fun onAvatarClick()
    }
}
