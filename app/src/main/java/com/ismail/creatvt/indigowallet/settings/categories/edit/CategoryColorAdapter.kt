package com.ismail.creatvt.indigowallet.settings.categories.edit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.ColorItemLayoutBinding
import com.ismail.creatvt.indigowallet.utility.ColorFactory.colors
import com.ismail.creatvt.indigowallet.utility.inflate

class CategoryColorAdapter : RecyclerView.Adapter<CategoryColorAdapter.ColorItemViewHolder>() {

    class ColorItemViewHolder(val itemBinding: ColorItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ColorItemViewHolder(
        parent.inflate(R.layout.color_item_layout)
    )

    override fun onBindViewHolder(holder: ColorItemViewHolder, position: Int) {
        holder.itemBinding.color = colors[position]
    }

    override fun getItemCount() = colors.size
}