package com.ejstudio.bookhistory.presentation.view.adapter.main.mybookhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.R
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.databinding.*
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.BookListDiffUtil
import com.ejstudio.bookhistory.presentation.view.DiffUtil.main.CalendarDateMemoDiffUtil
import com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch.AlwaysPopularBookAdapter





//class CalendarDateMemoAdapter {
//}

class CalendarDateMemoAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEWTYPE_TEXTMEMO = 0
    val VIEWTYPE_IMAGEMEMO = 1


    private lateinit var listener1: OnImageMemoClickListener
    interface OnImageMemoClickListener{
        fun onItemClick(holder: CalendarDateMemoAdapter.ImageMemoViewHolder?, view: View?, position:Int)
    }
    fun setOnOnImageMemoClickListener(listener: OnImageMemoClickListener){
        this.listener1 = listener
    }

    private lateinit var listener2: OnTextMemoClickListener
    interface OnTextMemoClickListener{
        fun onItemClick(holder: CalendarDateMemoAdapter.TextMemoViewHolder?, view: View?, position:Int)
    }
    fun setOnTextMemoClickListener(listener: OnTextMemoClickListener){
        this.listener2 = listener
    }

    private var items = ArrayList<TextImageMemoModel>()

    inner class ImageMemoViewHolder(binding : DateImageMemoItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            binding.root.setOnClickListener {
                var position = adapterPosition
                if(listener1 != null && position != RecyclerView.NO_POSITION) {
                    listener1.onItemClick(this, itemView, adapterPosition)
                }
            }
        }
        fun bindImage(info : TextImageMemoModel){
            binding.textImageMemoModel = info
        }
    }

    inner class TextMemoViewHolder(binding : DateTextMemoItemBinding) : RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            binding.root.setOnClickListener {
                var position = adapterPosition
                if(listener2 != null && position != RecyclerView.NO_POSITION) {
                    listener2.onItemClick(this, itemView, adapterPosition)
                }
            }
        }
        fun bindText(info : TextImageMemoModel){
            binding.textImageMemoModel = info
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var binding: Any?
        when (viewType) {
            VIEWTYPE_IMAGEMEMO -> {
                binding =
                    DateImageMemoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ImageMemoViewHolder(binding)
            }
            VIEWTYPE_TEXTMEMO -> {
                binding =
                    DateTextMemoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return TextMemoViewHolder(binding)
            }
            else -> {
                return TextMemoViewHolder(DateTextMemoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.binding.root.animation = AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.attach_in)

        if (holder is ImageMemoViewHolder) {
            holder.bindImage(items.get(position))
        } else if (holder is TextMemoViewHolder) {
            holder.bindText(items.get(position))
        }
    }

    fun updataList(newList: List<TextImageMemoModel>) {
        val tileDiffUtilCallback = CalendarDateMemoDiffUtil(items, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        var viewType = 0

        var aBoolean = items.get(position).memo_contents.contains("http://ec2-3-35-209-180.ap-northeast-2.compute.amazonaws.com/bookhistory/image/imageMemo/")

        if(aBoolean) {
            viewType = VIEWTYPE_IMAGEMEMO
        } else {
            viewType = VIEWTYPE_TEXTMEMO
        }

//        when (items.get(position).memo_contents.substring(0, 61)) {
//            "http://edit0@edit0.dothome.co.kr/bookhistory/image/imageMemo/" -> {
//                viewType = VIEWTYPE_IMAGEMEMO
//            }
//            else -> {
//                viewType = VIEWTYPE_TEXTMEMO
//            }
//        }
        return viewType
    }
}