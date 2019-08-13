package com.example.kotlinsamples.scores.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinsamples.R
import com.example.kotlinsamples.databinding.ItemListBinding
import com.example.kotlinsamples.scores.data.Score
import kotlinx.android.synthetic.main.item_list.view.*

class ScoreRecyclerAdapter(scores:ArrayList<Score>): RecyclerView.Adapter<ScoreRecyclerAdapter.RecyclerViewHolder>() {

    private var listScores: List<Score> = scores

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var currentScore: Score = listScores[position]

        holder.mName.text= currentScore.name
        holder.mScore.text = currentScore.score.toString()
    }

    override fun getItemCount(): Int {
        return listScores.size
    }

    fun addScores( list : List<Score>){
        this.listScores = list
        //the most important here
        notifyDataSetChanged()
    }

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mName = itemView.name_score
        var mScore = itemView.score_score

    }
}

/*
// bindingだと幅の認識はうまくいかない
class ScoreRecyclerAdapter(scores: ArrayList<Score>): RecyclerView.Adapter<ScoreRecyclerAdapter.ViewHolder>() {

    // 全体オブジェクト
    private var listScores: List<Score> = scores

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context)), onClickListener)
    }

    interface OnClickListener {
        fun onClick(score: Score)
    }
    var onClickListener: OnClickListener? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listScores.get(position))
    }

    class ViewHolder(private val binding: ItemListBinding, private val onClickListener: OnClickListener?) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Score) {
            binding.base.setOnClickListener {
                onClickListener?.onClick(item)
            }
            binding.name = item.name
            binding.score = item.score.toString()
        }
    }

    // 総数
    override fun getItemCount(): Int {
        return listScores.size
    }

    // 追加メソッド 任意
    fun addScores( list : List<Score>){
        this.listScores = list
        //the most important here
        notifyDataSetChanged()
    }
}
*/