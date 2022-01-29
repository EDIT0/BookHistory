package com.ejstudio.bookhistory.presentation.view.adapter.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.databinding.SearchBookItemBinding
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.SearchBookDiffUtil

class SearchBookAdapter : RecyclerView.Adapter<SearchBookAdapter.ViewHolder>() {

    private lateinit var listener1: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(holder: SearchBookAdapter.ViewHolder?, view: View?, position:Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener1 = listener
    }

    private var items = ArrayList<SearchBookModel.Document>()

    inner class ViewHolder(binding : SearchBookItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            itemView.setOnClickListener {
                var position = adapterPosition
                if(listener1 != null && position != RecyclerView.NO_POSITION) {
                    listener1.onItemClick(this, itemView, adapterPosition)
                }
            }
        }

        fun bind(info : SearchBookModel.Document){
            binding.searchBookModel = info
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchBookItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    fun updataList(newList: List<SearchBookModel.Document>) {
        val tileDiffUtilCallback = SearchBookDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}


//class SearchBookAdapter : RecyclerView.Adapter<SearchBookAdapter.ViewHolder>() {
//    private var items = ArrayList<SearchBookModel.Document>()
//
//    inner class ViewHolder(binding : SearchBookItemBinding) : RecyclerView.ViewHolder(binding.root){
//        var binding = binding
//
////        init {
////            itemView.setOnClickListener {
////                var position = adapterPosition
////                if(listener != null && position != RecyclerView.NO_POSITION) {
////                    listener.onItemClick(this, itemView, adapterPosition)
////                }
////            }
////        }
//
//        fun bind(info : SearchBookModel.Document){
//            binding.searchBookModel = info
//        }
//
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        var binding = SearchBookItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(items.get(position))
//    }
//
//    fun updataList(newList: List<SearchBookModel.Document>) {
//        val tileDiffUtilCallback = SearchBookDiffUtil(items, newList)
//        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
//        diffResult.dispatchUpdatesTo(this)
//        items.clear()
//        items.addAll(newList)
//    }
//}