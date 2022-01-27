package com.ejstudio.bookhistory.presentation.view.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.databinding.RecentSearchesItemBinding
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.RecentSearchesDiffUtil

class RecentSearchesAdapter: RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder>() {
    private var items = ArrayList<RecentSearchesEntity>()

    inner class ViewHolder(binding : RecentSearchesItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

//        init {
//            itemView.setOnClickListener {
//                var position = adapterPosition
//                if(listener != null && position != RecyclerView.NO_POSITION) {
//                    listener.onItemClick(this, itemView, adapterPosition)
//                }
//            }
//        }

        fun bind(info : RecentSearchesEntity){
            binding.searchWord.text = info.searchs
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RecentSearchesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    fun updataList(newList: List<RecentSearchesEntity>) {
        val tileDiffUtilCallback = RecentSearchesDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}