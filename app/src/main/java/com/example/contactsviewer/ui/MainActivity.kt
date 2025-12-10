package com.example.contactsviewer.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
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
import com.example.contactsviewer.data.ContactDao
import com.example.contactsviewer.data.ContactDbHelper
import com.example.contactsviewer.data.ContactRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var adapter: ContactAdapter? = null
    private val viewModel: ContactViewModel by viewModels {
        ContactViewModelFactory(ContactRepository(ContactDao(ContactDbHelper(this))))
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
            val dialog = AddContactDialog { newContact ->
                viewModel.addContact(newContact)
            }
            dialog.show(supportFragmentManager, "AddContactDialog")
        }
    }

    private fun initRecyclerView() {
        val recyclerView = this.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        adapter = ContactAdapter()
        adapter?.setListener(object : ContactAdapter.Listener {
            override fun onDeleteContact(contactId: Long) {
                viewModel.deleteContact(contactId)
            }
        })

        recyclerView.setAdapter(adapter)
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