package com.example.contactsviewer.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactsviewer.R;
import com.example.contactsviewer.model.Contact;

import java.io.File;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> data;
    private Listener listener;

    @SuppressLint("NotifyDataSetChanged")
    public void submitData(List<Contact> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_contact, parent, false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = data.get(position);

        loadAvatarImage(holder, contact);
        holder.contactName.setText(contact.getName());
        holder.contactNumber.setText(contact.getPhoneNumber());
        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (contact.getId() != null) {
                    data.remove(currentPosition);
                    listener.onDeleteContact(contact.getId());
                    notifyItemRemoved(currentPosition);
                }
            }
        });
        holder.avatarImage.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (contact.getId() != null) {
                    listener.onAvatarClick(contact.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static void loadAvatarImage(@NonNull ContactViewHolder holder, Contact contact) {
        if (contact.getAvatarPath() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(new File(contact.getAvatarPath()))
                    .circleCrop()
                    .placeholder(R.drawable.outline_account_circle_24)
                    .into(holder.avatarImage);
        } else {
            holder.avatarImage.setImageResource(R.drawable.outline_account_circle_24);
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarImage;
        private final TextView contactName;
        private final TextView contactNumber;
        private final ImageButton deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.image_avatar);
            contactName = itemView.findViewById(R.id.text_contact_name);
            contactNumber = itemView.findViewById(R.id.text_contact_number);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }

    interface Listener {
        void onDeleteContact(long contactId);

        void onAvatarClick(long contactId);
    }
}
