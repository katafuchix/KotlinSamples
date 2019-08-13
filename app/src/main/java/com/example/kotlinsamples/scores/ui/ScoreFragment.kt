package com.example.kotlinsamples.scores.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.example.kotlinsamples.R
import com.example.kotlinsamples.databinding.FragmentScoreBinding
import com.example.kotlinsamples.scores.data.Score
import com.example.kotlinsamples.scores.data.ScoreDataBase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.view.MenuInflater
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoreFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ScoreFragment : Fragment() {

    private var scoreRecyclerAdapter: ScoreRecyclerAdapter?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = FragmentScoreBinding.inflate(inflater, container, false)

        scoreRecyclerAdapter = ScoreRecyclerAdapter(arrayListOf())
        binding.recyclerView.adapter = scoreRecyclerAdapter

        binding.addNewScore.setOnClickListener {
            addScore(binding.nameEditText.text.toString(), binding.socreEditText.text.toString().toInt())
        }

        // DBに追加されたデータを含めすべてのデータ表示
        ScoreDataBase.getInstance(requireContext()).scoreDao().getAllScores()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listScores->
                scoreRecyclerAdapter!!.addScores(listScores)
            }

        return binding.root

    }

    fun addScore(name:String, score:Int){
        val score = Score(0, name, score)
        // 非同期処理の待ち合わせのコード でデータを追加
        Single.fromCallable {
            ScoreDataBase.getInstance(requireContext()).scoreDao().insertScore(score)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_score, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.clear_list_action->
                deleteAllScores()
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteAllScores(){
        // Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
        // 対策
        Completable.fromAction { ScoreDataBase.getInstance(requireContext()).scoreDao().deleteAllScore() }
            .subscribeOn(Schedulers.io())
            .subscribe()

    }

    companion object {
        fun newInstance(): ScoreFragment {
            // インスタンス生成
            val fragment = ScoreFragment()
            return fragment
        }
    }
}
