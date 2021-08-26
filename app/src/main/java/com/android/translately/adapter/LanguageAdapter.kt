package com.android.translately.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.translately.R
import com.android.translately.databinding.ItemLanguageBinding
import com.android.translately.model.language.Language
import java.util.ArrayList

class LanguageAdapter(val context: Context, var data: ArrayList<Language>?, val onclick: onClick) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_language, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data != null) {
            if (data!!.size > 0) {
                holder.binding.languageModel = data!!.get(position)
                holder.binding.languageImage = context.resources.getIdentifier(
                    "flag_" +
                            data!!.get(position).languageCode,
                    "drawable",
                    context.packageName
                )

                holder.itemView.setOnClickListener {
                    onclick.onClick(data!!.get(position))
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
    fun filterList(filterdNames: ArrayList<Language>) {
        data = filterdNames
        notifyDataSetChanged()
    }
    interface onClick {
        fun onClick(language: Language)
    }
}