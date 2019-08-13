package com.example.kotlinsamples.retrofitrx.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlinsamples.databinding.FragmentRetrofitRxBinding
import com.example.kotlinsamples.extension.autoCleared
import com.example.kotlinsamples.retrofitrx.api.GithubApi
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import com.kaopiz.kprogresshud.KProgressHUD

class RetrofitRxFragment : Fragment() {

    private var binding by autoCleared<FragmentRetrofitRxBinding>()
    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_retrofit_rx, container, false)

        binding = FragmentRetrofitRxBinding.inflate(inflater, container, false)
        return binding.root.apply {
            hud = KProgressHUD.create(this.context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            hud?.show()

            val retrofit = Retrofit.Builder()
                .baseUrl(GithubApi.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

            val service = retrofit.create(GithubApi::class.java)
            service.getUser("katafuchix").subscribe({ ret ->
                Log.d(TAG, "ret $ret")
                binding.nameText.text  = ret.name
                binding.loginText.text = ret.login
                binding.blogText.text  = ret.blog
                binding.typeText.text  = ret.type
                hud?.dismiss()

            }, { error ->
                Log.e(TAG, error.message)
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RetrofitRxFragment()

        val TAG = RetrofitRxFragment::class.java.simpleName
    }
}
