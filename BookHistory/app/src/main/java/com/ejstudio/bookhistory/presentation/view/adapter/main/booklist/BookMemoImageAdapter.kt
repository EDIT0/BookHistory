package com.ejstudio.bookhistory.presentation.view.adapter.main.booklist

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.model.ImageMemoEntity

import com.ejstudio.bookhistory.databinding.BookMemoImageItemBinding

import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.BookMemoImageDiffUtil


class BookMemoImageAdapter: RecyclerView.Adapter<BookMemoImageAdapter.ViewHolder>() {

    private val TAG = BookMemoImageAdapter::class.java.simpleName

    private lateinit var listener1: OnBookMemoImageClickListener

    interface OnBookMemoImageClickListener{
        fun onItemClick(holder: BookMemoImageAdapter.ViewHolder?, view: View?, position:Int)
    }

    fun setOnBookMemoImageClickListener(listener: OnBookMemoImageClickListener){
        this.listener1 = listener
    }

    private var items = ArrayList<ImageMemoEntity>()
    private var width: Int = 0
    private var height: Int = 0

    inner class ViewHolder(binding : BookMemoImageItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            binding.root.setOnClickListener {
                var position = adapterPosition
                if(listener1 != null && position != RecyclerView.NO_POSITION) {
                    listener1.onItemClick(this, itemView, adapterPosition)
                }
            }


//            val layoutParams1: ViewGroup.LayoutParams = binding.ibImageMemo.getLayoutParams()
//            layoutParams1.width = width
//            layoutParams1.height = width
//            binding.ibImageMemo.setLayoutParams(layoutParams1)
            val layoutParams2: ViewGroup.LayoutParams = binding.cardView.getLayoutParams()
//            layoutParams2.width = width
            layoutParams2.height = width
            binding.cardView.setLayoutParams(layoutParams2)
        }

        fun bind(info : ImageMemoEntity){
            Log.i(TAG, "이미지url: " + info.memo_image)
            Glide.with(binding.root)
                .load(info.memo_image)
                .error(R.drawable.img_bookcover_null)
                .override(width, width)
                .into(binding.ibImageMemo)
//            binding.ibImageMemo.setImageURI(Uri.parse(ApiClient.IMAGE_BASE_URL + info.memo_image))
//            binding.imageMemoEntity = info
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = BookMemoImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
        holder.bind(items.get(position))
        holder.binding.root.animation = AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.fade_in)
    }

    fun updataList(newList: List<ImageMemoEntity>) {
        val tileDiffUtilCallback = BookMemoImageDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setWidth(x: Int) {
        width = x / 2
    }
}