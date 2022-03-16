package com.ejstudio.bookhistory.presentation.view.adapter.main.booklist

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.databinding.BookListItemBinding
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.BookListDiffUtil
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan


class BookListAdapter: RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    lateinit var builder: SpannableStringBuilder
    lateinit var colorBlueSpan: ForegroundColorSpan

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

            when(info.reading_state) {
                MainViewModel.READING -> {
                    binding.tvStateDate.text = info.reading_start_datetime?.substring(0,10) + " 부터 읽는 중"

//                    builder = SpannableStringBuilder(binding.tvStateDate.text.toString())
//                    colorBlueSpan = ForegroundColorSpan(Color.parseColor("#ffa967"))
//                    builder.setSpan(colorBlueSpan, 11, binding.tvStateDate.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//                    binding.tvStateDate.text = builder
                }
                MainViewModel.BEFORE_READ -> {
                    binding.tvStateDate.text = info.add_datetime?.substring(0,10) + " 리스트에 담음"

//                    builder = SpannableStringBuilder(binding.tvStateDate.text.toString())
//                    colorBlueSpan = ForegroundColorSpan(Color.parseColor("#ffa967"))
//                    builder.setSpan(colorBlueSpan, 11, binding.tvStateDate.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//                    binding.tvStateDate.text = builder
                }
                MainViewModel.END_READ -> {
                    if(info.reading_start_datetime == null || info.reading_start_datetime.length == 0 || info.reading_start_datetime.substring(0, 10).equals("0001-01-01")) {
                        binding.tvStateDate.text = info.reading_end_datetime?.substring(0, 10) + " ~ " + info.reading_end_datetime?.substring(0, 10)
                    } else {
                        binding.tvStateDate.text = info.reading_start_datetime?.substring(0, 10) + " ~ " + info.reading_end_datetime?.substring(0, 10)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = BookListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
        holder.binding.root.animation = AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.attach_in)
    }

    fun updataList(newList: List<BookListEntity>) {
        val tileDiffUtilCallback = BookListDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}