package com.ismail.creatvt.indigowallet.icons

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.CategoryIconItemBinding
import com.ismail.creatvt.indigowallet.databinding.IconItemLayoutBinding
import com.ismail.creatvt.indigowallet.utility.IconFactory
import com.ismail.creatvt.indigowallet.utility.inflate

class IconAdapter(val icons: List<IconFactory.Icon>, val iconClickListener: IconClickListener) : RecyclerView.Adapter<IconAdapter.CategoryIconViewHolder>() {

    class CategoryIconViewHolder(val itemBinding: IconItemLayoutBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryIconViewHolder(
        parent.inflate(R.layout.icon_item_layout)
    )

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        val icon = icons[position]
        holder.itemBinding.icon = icon
        holder.itemBinding.listener = iconClickListener
    }

    override fun getItemCount() = icons.size

}