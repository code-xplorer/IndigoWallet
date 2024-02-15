package com.ismail.creatvt.indigowallet.settings.categories

import androidx.recyclerview.widget.DiffUtil
import com.ismail.creatvt.indigowallet.db.entity.Category

class CategoryDiffCallback(val oldItems: List<Category>, val newItems: List<Category>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    private fun getItems(oldItemPosition: Int, newItemPosition: Int) =
        Pair(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItems(oldItemPosition, newItemPosition)
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItems(oldItemPosition, newItemPosition)
        return oldItem.id == newItem.id && oldItem.name == newItem.name
                && oldItem.icon == newItem.icon && oldItem.color == newItem.color
    }
}