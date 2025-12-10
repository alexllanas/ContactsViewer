package com.example.contactsviewer.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsviewer.R
import com.example.contactsviewer.data.ContactDatabase
import com.example.contactsviewer.data.ContactDbHelper
import com.example.contactsviewer.data.ContactRepository
import com.example.contactsviewer.model.Contact
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var adapter: ContactAdapter? = null
    private var selectedContactId: Long? = null

    private val viewModel: ContactViewModel by viewModels {
        ContactViewModelFactory(ContactRepository(ContactDatabase(ContactDbHelper(this))))
    }

    private val imageGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri != null && selectedContactId != null) {
                viewModel.saveAvatarImage(this, imageUri, selectedContactId!!)
            }
        }

    private val listener = object : ContactAdapter.Listener {
        override fun onDeleteContact(contactId: Long) {
            viewModel.deleteContact(contactId)
        }

        override fun onAvatarClick(contactId: Long) {
            launchImageGallery(contactId = contactId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        this.setContentView(R.layout.activity_main)
        setWindowInsets()

        initUI()
        loadContacts()
    }

    private fun initUI() {
        setupToolbar()
        initRecyclerView()
        setupClickListeners()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar.isTitleCentered = true
        setSupportActionBar(toolbar)
    }

    private fun setupClickListeners() {
        findViewById<FloatingActionButton>(R.id.fab_add_contact).setOnClickListener {
            val dialog = AddContactDialog(object : AddContactDialog.Listener {
                override fun onAdd(contact: Contact, selectedImageUri: Uri?) {
                    viewModel.addContact(context = this@MainActivity, contact, selectedImageUri)
                }

                override fun onAvatarClick() {
                    launchImageGallery(contactId = null)
                }
            })
            dialog.show(supportFragmentManager, "AddContactDialog")
        }
    }

    private fun initRecyclerView() {
        val recyclerView = this.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        adapter = ContactAdapter()
        adapter?.setListener(listener)

        recyclerView.setAdapter(adapter)
    }

    private fun launchImageGallery(contactId: Long?) {
        selectedContactId = contactId
        imageGalleryLauncher.launch("image/*")
    }

    private fun loadContacts() {
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.contacts.collect { contacts ->
                    adapter?.submitData(contacts)
                }
            }
        }
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            this.findViewById(R.id.main)
        ) { v: View?, insets: WindowInsetsCompat? ->
            val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
            v?.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}