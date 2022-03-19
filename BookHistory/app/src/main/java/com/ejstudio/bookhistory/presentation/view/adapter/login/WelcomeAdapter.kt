package com.ejstudio.bookhistory.presentation.view.adapter.login

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R

import com.ejstudio.bookhistory.databinding.WelcomeItemBinding
import com.ejstudio.bookhistory.domain.model.WelcomeModel


class WelcomeAdapter(
    var items: List<WelcomeModel>
) : RecyclerView.Adapter<WelcomeAdapter.ViewHolder>() {

    inner class ViewHolder(binding : WelcomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding

        fun bind(info: WelcomeModel) {
            Glide.with(binding.root.context)
                .load(info.img_welcome)
                .error(R.drawable.img_bookcover_null)
                .into(binding.ivWelcome)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeAdapter.ViewHolder {
        var binding = WelcomeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WelcomeAdapter.ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }
}