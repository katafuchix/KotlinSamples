package com.example.kotlinsamples.shortenurl.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kotlinsamples.BuildConfig

import com.example.kotlinsamples.R
import com.example.kotlinsamples.databinding.FragmentShortenUrlBinding
import com.example.kotlinsamples.extension.autoCleared
import com.example.kotlinsamples.shortenurl.api.Api
import com.example.kotlinsamples.shortenurl.model.NetworkRepositoryImpl
import com.example.kotlinsamples.shortenurl.utils.baseUrl
import com.example.kotlinsamples.shortenurl.viewmodel.ShortenUrlViewModel
import com.example.kotlinsamples.shortenurl.utils.copyToClipBoard
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ShortenUrlFragment : Fragment() {

    // DIを利用しないパターン 長いので改善したい
    private val viewModel
            by lazy {
                val api = Retrofit.Builder()
                            .client(
                                OkHttpClient.Builder()
                                    .addInterceptor(
                                        Interceptor { chain ->
                                            val builder = chain.request().newBuilder().apply {
                                                header("X-Naver-Client-Id", getString(R.string.naver_client_id))
                                                header("X-Naver-Client-Secret", getString(R.string.naver_api_key))
                                            }
                                            chain.proceed(builder.build())
                                        }
                                    )
                                    .addInterceptor(
                                        HttpLoggingInterceptor().apply {
                                            level = if (BuildConfig.DEBUG)
                                                HttpLoggingInterceptor.Level.BASIC
                                            else
                                                HttpLoggingInterceptor.Level.NONE
                                        } as Interceptor
                                    )
                                    .build()
                            )
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(baseUrl)
                            .build()
                            .create(Api::class.java)

                ViewModelProviders.of(
                    this,
                    ShortenUrlViewModel.Factory(
                        NetworkRepositoryImpl(api)
                    )
                ).get(ShortenUrlViewModel::class.java)
            }

    // 置き換え可能
    //  private val shortenUrlViewModelFactory: ShortenUrlViewModelFactory by inject()
    //  val shortenUrlViewModel = ViewModelProviders.of(requireContext(), shortenUrlViewModelFactory).get(ShortenUrlViewModel::class.java)

    private var binding by autoCleared<FragmentShortenUrlBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_shorten_url, container, false)
        binding = FragmentShortenUrlBinding.inflate(inflater, container, false)
        binding.urlEditText.addValidator(viewModel.getUrlValidator(getString(R.string.error_validate_email)))
        binding.shortenUrlViewModel = viewModel
        binding.setLifecycleOwner(this)

        binding.button.setOnClickListener {
            viewModel.getShortenUrl(binding.urlEditText.text.toString())
        }

        viewModel.error.observe(this, Observer<String> { t ->
            Toast.makeText(requireContext(), t, Toast.LENGTH_LONG).show()
        })

        binding.btnCopyToClipboard.setOnClickListener {
            viewModel.shortenUrl.value?.apply {
                val clipUrl = this
                requireContext().copyToClipBoard(clipUrl) {
                    Toast.makeText(requireContext(), getString(R.string.clipboard_copied, clipUrl), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnOpenWeb.setOnClickListener {
            viewModel.shortenUrl.value?.apply {
                val clipUrl = this
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(clipUrl)))
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShortenUrlFragment()
    }
}
