package com.example.kotlinsamples.contact.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kotlinsamples.R
import com.example.kotlinsamples.contact.ContactDetailsActivity
import com.example.kotlinsamples.contact.data.Contact
import com.example.kotlinsamples.contact.data.ContactDataBase
import com.example.kotlinsamples.contact.viewmodel.ContactViewModel
import com.example.kotlinsamples.databinding.FragmentContactBinding
import com.example.kotlinsamples.scores.data.ScoreDataBase
import com.example.kotlinsamples.scores.ui.ScoreRecyclerAdapter
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ContactFragment : Fragment(), ContactRecyclerAdapter.OnItemClickListener {

    private val viewModel
            by lazy {
                ViewModelProviders.of(
                    this,
                    ContactViewModel.Factory(
                        ContactDataBase.getDataBase(this.context!!).contactDao()
                    )
                ).get(ContactViewModel::class.java)
            }

    //private var db: ContactDataBase = ContactDataBase.getDataBase(requireContext())
    private var recyclerViewAdapter: ContactRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        val binding = FragmentContactBinding.inflate(inflater, container, false)

        recyclerViewAdapter = ContactRecyclerAdapter(arrayListOf(), this)
        binding.recyclerView.adapter =recyclerViewAdapter

        binding.fab.setOnClickListener {
            var intent = Intent(requireContext(), ContactDetailsActivity::class.java)
            startActivity(intent)
        }
        viewModel.getListContacts().observe(this, Observer { contacts ->
            recyclerViewAdapter!!.addContacts(contacts!!)
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_contact, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.delete_all_items->
                deleteAllScores()
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteAllScores(){
        // Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
        // 対策
        Completable.fromAction { ContactDataBase.getDataBase(requireContext()).contactDao().deleteAllContacts() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onItemClick(contact: Contact) {
        var intent = Intent(requireContext(), ContactDetailsActivity::class.java)
        intent.putExtra("idContact", contact.id)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ContactFragment()
    }
}
