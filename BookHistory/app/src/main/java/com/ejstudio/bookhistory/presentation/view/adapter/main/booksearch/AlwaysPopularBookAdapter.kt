package com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.databinding.AlwaysPopularBookItemBinding
import com.ejstudio.bookhistory.databinding.RecentPopularBookItemBinding
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.RecentPopularBookDiffUtil

/*
* RecentPopularBookModel.class와 데이터가 같으므로 공유하여 사용
* */
class AlwaysPopularBookAdapter : RecyclerView.Adapter<AlwaysPopularBookAdapter.ViewHolder>() {

    private lateinit var listener1: OnAlwaysPopularBookClickListener

    interface OnAlwaysPopularBookClickListener{
        fun onItemClick(holder: ViewHolder?, view: View?, position:Int)
    }

    fun setOnAlwaysPopularBookClickListener(listener: OnAlwaysPopularBookClickListener){
        this.listener1 = listener
    }

    private var items = ArrayList<RecentPopularBookModel.Response.Doc>()

    inner class ViewHolder(binding : AlwaysPopularBookItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            itemView.setOnClickListener {
                var position = adapterPosition
                if(listener1 != null && position != RecyclerView.NO_POSITION) {
                    listener1.onItemClick(this, itemView, adapterPosition)
                }
            }
        }

        fun bind(info : RecentPopularBookModel.Response.Doc.Doc__1){
            binding.recentPopularBookModel = info
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = AlwaysPopularBookItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position).doc)
    }

    fun updataList(newList: List<RecentPopularBookModel.Response.Doc>) {
        val tileDiffUtilCallback = RecentPopularBookDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}