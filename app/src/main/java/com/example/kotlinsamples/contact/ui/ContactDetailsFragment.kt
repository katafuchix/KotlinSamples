package com.example.kotlinsamples.contact.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

import com.example.kotlinsamples.R
import com.example.kotlinsamples.contact.data.Contact
import com.example.kotlinsamples.contact.data.ContactDataBase
import com.example.kotlinsamples.contact.viewmodel.ContactViewModel
import com.example.kotlinsamples.databinding.FragmentContactDetailsBinding
import com.example.kotlinsamples.extension.autoCleared

class ContactDetailsFragment : Fragment() {

    private val viewModel
            by lazy {
                ViewModelProviders.of(
                    this,
                    ContactViewModel.Factory(
                        ContactDataBase.getDataBase(this.context!!).contactDao()
                    )
                ).get(ContactViewModel::class.java)
            }

    private var binding by autoCleared<FragmentContactDetailsBinding>()
    private var currentContact: Int? = null
    private var contact: Contact? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        // 引数を受け取る
        val bundle = arguments
        currentContact = bundle!!.getInt("currentContact")

        binding = FragmentContactDetailsBinding.inflate(inflater, container, false)

        if (currentContact != -1) {
            getActivity()?.setTitle(R.string.edit_contact_title)
            contact =  ContactDataBase.getDataBase(this.context!!).contactDao().getContactById(currentContact!!)
            binding.nameEditText.setText(contact!!.name)
            binding.numberEditText.setText(contact!!.number)
        } else {
            getActivity()?.setTitle(R.string.add_contact_title)
        }

        return return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_contact_details, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (currentContact == -1) {
            menu.findItem(R.id.delete_item).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.done_item -> {
                if (currentContact == -1) {
                    saveContact()
                    Toast.makeText(requireContext(), getString(R.string.save_contact), Toast.LENGTH_SHORT).show()
                } else {
                    updateContact()
                    Toast.makeText(requireContext(), getString(R.string.update_contact), Toast.LENGTH_SHORT).show()
                }
                activity?.finish()
            }
            R.id.delete_item -> {
                deleteContact()
                Toast.makeText(requireContext(), getString(R.string.delete_contact), Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveContact() {
        var nameContact = binding.nameEditText.text.toString()
        var numberContact = binding.numberEditText.text.toString()
        var contact = Contact(0, nameContact, numberContact)
        viewModel.addContact(contact)
    }

    private fun updateContact() {
        var nameContact = binding.nameEditText.text.toString()
        var numberContact = binding.numberEditText.text.toString()
        var contact = Contact(contact!!.id, nameContact, numberContact)
        viewModel.updateContact(contact)
    }

    private fun deleteContact() {
        viewModel.deleteContact(contact!!)
    }

    companion object {
        fun newInstance(currentContact: Int): ContactDetailsFragment {

            // インスタンス生成
            val fragment =  ContactDetailsFragment()

            // Bundle にパラメータを設定
            val args = Bundle()
            args.putInt("currentContact", currentContact)
            fragment.setArguments(args)

            return fragment
        }
    }
}
