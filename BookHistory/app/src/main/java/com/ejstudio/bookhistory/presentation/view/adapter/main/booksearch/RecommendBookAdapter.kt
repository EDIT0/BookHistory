package com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.databinding.RecentPopularBookItemBinding
import com.ejstudio.bookhistory.databinding.RecommendBookItemBinding
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.RecommendBookModel
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.RecentPopularBookDiffUtil
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.RecommendBookDiffUtil

class RecommendBookAdapter : RecyclerView.Adapter<RecommendBookAdapter.ViewHolder>() {

    private lateinit var listener1: OnRecommendBookClickListener

    interface OnRecommendBookClickListener{
        fun onItemClick(holder: ViewHolder?, view: View?, position:Int)
    }

    fun setOnRecommendBookClickListener(listener: OnRecommendBookClickListener){
        this.listener1 = listener
    }

    private var items = ArrayList<RecommendBookModel.Response.Doc>()

    inner class ViewHolder(binding : RecommendBookItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            itemView.setOnClickListener {
                var position = adapterPosition
                if(listener1 != null && position != RecyclerView.NO_POSITION) {
                    listener1.onItemClick(this, itemView, adapterPosition)
                }
            }
        }

        fun bind(info : RecommendBookModel.Response.Doc.Book){
            binding.recommendBookModel = info
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RecommendBookItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position).book)
    }

    fun updataList(newList: List<RecommendBookModel.Response.Doc>) {
        val tileDiffUtilCallback = RecommendBookDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}