package com.ejstudio.bookhistory.presentation.view.adapter.main.booklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.databinding.BookMemoTextItemBinding
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.BookMemoTextDiffUtil

class BookMemoTextAdapter: RecyclerView.Adapter<BookMemoTextAdapter.ViewHolder>() {

    private lateinit var listener1: OnBookMemoTextClickListener

    interface OnBookMemoTextClickListener{
        fun onItemClick(holder: BookMemoTextAdapter.ViewHolder?, view: View?, position:Int)
    }

    fun setOnBookMemoTextClickListener(listener: OnBookMemoTextClickListener){
        this.listener1 = listener
    }

    private var items = ArrayList<TextMemoEntity>()

    inner class ViewHolder(binding : BookMemoTextItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            binding.root.setOnClickListener {
                var position = adapterPosition
                if(listener1 != null && position != RecyclerView.NO_POSITION) {
                    listener1.onItemClick(this, itemView, adapterPosition)
                }
            }
        }

        fun bind(info : TextMemoEntity){
            binding.textMemoEntity = info
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = BookMemoTextItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
        holder.binding.root.animation = AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.fade_in)
    }

    fun updataList(newList: List<TextMemoEntity>) {
        val tileDiffUtilCallback = BookMemoTextDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}