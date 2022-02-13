package com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.RecentPopularBookItemBinding
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.RecentPopularBookDiffUtil

class RecentPopularBookAdapter : RecyclerView.Adapter<RecentPopularBookAdapter.ViewHolder>() {

    private lateinit var listener1: OnRecentPopularBookClickListener

    interface OnRecentPopularBookClickListener{
        fun onItemClick(holder: ViewHolder?, view: View?, position:Int)
    }

    fun setOnRecentPopularBookClickListener(listener: OnRecentPopularBookClickListener){
        this.listener1 = listener
    }

    private var items = ArrayList<RecentPopularBookModel.Response.Doc>()

    inner class ViewHolder(binding : RecentPopularBookItemBinding) : RecyclerView.ViewHolder(binding.root){
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
        var binding = RecentPopularBookItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position).doc)
        holder.binding.root.animation = AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.fade_in)
    }

    fun updataList(newList: List<RecentPopularBookModel.Response.Doc>) {
        val tileDiffUtilCallback = RecentPopularBookDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}