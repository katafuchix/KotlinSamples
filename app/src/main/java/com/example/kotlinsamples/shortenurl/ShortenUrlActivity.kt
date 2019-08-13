package com.example.kotlinsamples.shortenurl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinsamples.R
import com.example.kotlinsamples.shortenurl.di.appModules
import com.example.kotlinsamples.shortenurl.ui.ShortenUrlFragment
import org.koin.android.ext.android.startKoin

class ShortenUrlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shorten_url)
        //startKoin(this, appModules)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // BackStackを設定
            fragmentTransaction.addToBackStack(null)

            val fragment = ShortenUrlFragment.newInstance()

            // idがcontainerの領域にフラグメントを表示
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
