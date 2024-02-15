package com.ismail.creatvt.indigowallet.settings.categories.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.CategoryIconItemBinding
import com.ismail.creatvt.indigowallet.utility.IconFactory
import com.ismail.creatvt.indigowallet.utility.inflate

class CategoryIconsAdapter(val iconName: String, val iconClickListener: IconClickListener) : RecyclerView.Adapter<CategoryIconsAdapter.CategoryIconViewHolder>() {

    class CategoryIconViewHolder(val itemBinding: CategoryIconItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryIconViewHolder(
        parent.inflate(R.layout.category_icon_item)
    )

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        val icon = IconFactory.categoryIcons[position]
        holder.itemBinding.icon = icon
        holder.itemBinding.isSelected = iconName == icon.name
        holder.itemBinding.listener = iconClickListener
    }

    override fun getItemCount() = IconFactory.categoryIcons.size

}