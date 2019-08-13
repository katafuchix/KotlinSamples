package com.example.kotlinsamples.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinsamples.R
import com.example.kotlinsamples.contact.ui.ContactDetailsFragment

class ContactDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (savedInstanceState == null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // BackStackを設定
            fragmentTransaction.addToBackStack(null)

            val currentContact = intent.getIntExtra("idContact", -1)
            val fragment = ContactDetailsFragment.newInstance(currentContact)

            // idがcontainerの領域にフラグメントを表示
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun setActionBarTitle(title: String) {
        actionBar!!.title = title
    }
}
