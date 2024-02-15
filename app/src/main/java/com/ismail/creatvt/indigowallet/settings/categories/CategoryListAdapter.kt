package com.ismail.creatvt.indigowallet.settings.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.CategoryListItemBinding
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.utility.inflate

class CategoryListAdapter(
    categories: LiveData<List<Category>>,
    val listener: CategoryClickListener,
    owner: LifecycleOwner
) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryItemViewHolder>() {

    var categoryList = listOf<Category>()
        set(value) {
            val oldItems = field
            field = value
            DiffUtil.calculateDiff(CategoryDiffCallback(oldItems, value))
                .dispatchUpdatesTo(this)
        }

    init {
        categories.observe(owner) {
            categoryList = it
        }
    }

    class CategoryItemViewHolder(val itemBinding: CategoryListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryItemViewHolder(
        parent.inflate(R.layout.category_list_item)
    )

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.itemBinding.category = categoryList[position]
        holder.itemBinding.listener = listener
    }

    override fun getItemCount() = categoryList.size
}