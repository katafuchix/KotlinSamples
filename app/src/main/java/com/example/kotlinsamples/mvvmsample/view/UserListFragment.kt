package com.example.kotlinsamples.mvvmsample.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room

import com.example.kotlinsamples.R
import com.example.kotlinsamples.databinding.FragmentUserListBinding
import com.example.kotlinsamples.extension.autoCleared
import com.example.kotlinsamples.mvvmsample.repository.UserRepository
import com.example.kotlinsamples.mvvmsample.repository.api.UserApi
import com.example.kotlinsamples.mvvmsample.repository.data.UserDataBase
import com.example.kotlinsamples.mvvmsample.viewmodel.UserListViewModel
import com.example.kotlinsamples.mvvmsample.viewmodel.data.UsersList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException

class UserListFragment : MvvmFragment() {

    private val viewModel
            by lazy {

                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://randomapi.com/api/")
                    .build()
                val userApi = retrofit.create(UserApi::class.java)

                val appDatabase = Room.databaseBuilder(requireContext(),
                    UserDataBase::class.java, "mvvm-database").build()

                ViewModelProviders.of(
                    this,
                    UserListViewModel.Factory(
                        UserRepository(userApi, appDatabase.userDao())
                    )
                ).get(UserListViewModel::class.java)
            }

    private var binding by autoCleared<FragmentUserListBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_list, container, false)

        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        subscribe(viewModel.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Received UIModel $it users.")
                showUsers(it)
            }, {
                Timber.w(it)
                showError()
            }))
    }

    fun showUsers(data: UsersList) {
        if (data.error == null) {
            binding.usersList.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data.users)
        } else if (data.error is ConnectException || data.error is UnknownHostException) {
            Timber.d("No connection, maybe inform user that data loaded from DB.")
        } else {
            showError()
        }
    }

    fun showError() {
        Toast.makeText(context, "An error occurred :(", Toast.LENGTH_SHORT).show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = UserListFragment()
    }
}
