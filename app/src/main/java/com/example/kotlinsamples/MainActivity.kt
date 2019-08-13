package com.example.kotlinsamples

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.kotlinsamples.contact.ContactActivity
import com.example.kotlinsamples.databinding.ActivityMainBinding
import com.example.kotlinsamples.mvvmsample.MvvmSampleActivity
import com.example.kotlinsamples.retrofitrx.RetrofitRxActivity
import com.example.kotlinsamples.scores.ScoresActivity
import com.example.kotlinsamples.shortenurl.ShortenUrlActivity

class MainActivity : AppCompatActivity() {

    //private val binding: ActivityMainBinding? = null

    val data = listOf(
        Data("Sample App RxJava and Room DB", ScoresActivity::class.java),
        Data("Sample App Room DB, RecyclerView, ViewModel", ContactActivity::class.java),
        Data("MVVM Rx Sample", MvvmSampleActivity::class.java),
        Data("Retrofit Rx Sample", RetrofitRxActivity::class.java),
        Data("MVVM, ViewModel, Rx, LiveData, Databinding, MaterialEditText Sample", ShortenUrlActivity::class.java)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.list.adapter = ArrayAdapter<Data>(this, android.R.layout.simple_list_item_1, data)
        binding.list.setOnItemClickListener({ _, _, position, _ ->
            startActivity(Intent(this, data[position].clazz))
        })
    }
}

data class Data(val name: String, val clazz: Class<out AppCompatActivity>) {
    override fun toString(): String {
        return name
    }
}