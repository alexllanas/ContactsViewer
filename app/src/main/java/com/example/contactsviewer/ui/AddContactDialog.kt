package com.example.contactsviewer.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.contactsviewer.databinding.DialogAddContactBinding
import com.example.contactsviewer.model.Contact

class AddContactDialog(
    private val onAdd: (Contact) -> Unit
) : DialogFragment() {

    private var _binding: DialogAddContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddContactBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())

        builder
            .setTitle("Add New Contact")
            .setView(binding.root)
            .create()

        binding.buttonAdd.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val phoneNumber = binding.editTextPhone.text.toString()
            if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val contact = Contact(
                    name = binding.editTextName.text.toString(),
                    phoneNumber = binding.editTextPhone.text.toString()
                )
                onAdd(contact)
                dismiss()
            }
        }

        binding.buttonCancel.setOnClickListener { dismiss() }

        return builder.create()
    }
}