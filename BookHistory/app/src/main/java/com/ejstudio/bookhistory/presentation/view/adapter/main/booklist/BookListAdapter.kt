package com.ejstudio.bookhistory.presentation.view.adapter.main.booklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.databinding.BookListItemBinding
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.BookListDiffUtil

class BookListAdapter: RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    private lateinit var listener1: OnBookClickListener

    interface OnBookClickListener{
        fun onItemClick(holder: BookListAdapter.ViewHolder?, view: View?, position:Int)
    }

    fun setOnBookClickListener(listener: OnBookClickListener){
        this.listener1 = listener
    }

    private var items = ArrayList<BookListEntity>()

    inner class ViewHolder(binding : BookListItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            binding.root.setOnClickListener {
                var position = adapterPosition
                if(listener1 != null && position != RecyclerView.NO_POSITION) {
                    listener1.onItemClick(this, itemView, adapterPosition)
                }
            }
        }

        fun bind(info : BookListEntity){
            binding.bookListEntity = info
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = BookListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    fun updataList(newList: List<BookListEntity>) {
        val tileDiffUtilCallback = BookListDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}