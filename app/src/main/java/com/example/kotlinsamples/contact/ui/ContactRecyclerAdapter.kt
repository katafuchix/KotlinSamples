package com.example.kotlinsamples.contact.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinsamples.R
import com.example.kotlinsamples.contact.data.Contact


class ContactRecyclerAdapter(contacts: ArrayList<Contact>, listener: OnItemClickListener) : RecyclerView.Adapter<ContactRecyclerAdapter.RecyclerViewHolder>() {

    private var listContacts: List<Contact> = contacts

    private var listenerContact: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(contact: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var currentContact: Contact = listContacts[position]

        var nameContact = currentContact.name
        var numberContact = currentContact.number

        holder!!.mName.text = nameContact
        holder!!.mNumber.text = numberContact

        holder.bind(currentContact, listenerContact)

    }

    fun addContacts(listContacts: List<Contact>) {
        this.listContacts = listContacts
        notifyDataSetChanged()
    }


    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mName = itemView.findViewById<TextView>(R.id.name_contact)!!
        var mNumber = itemView.findViewById<TextView>(R.id.number_contact)!!

        fun bind(contact: Contact, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(contact)
            }
        }

    }
}