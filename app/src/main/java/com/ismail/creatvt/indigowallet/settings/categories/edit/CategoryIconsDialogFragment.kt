package com.ismail.creatvt.indigowallet.settings.categories.edit

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.DataBindingBottomSheetDialogFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentCategoryIconsDialogBinding
import com.ismail.creatvt.indigowallet.utility.CATEGORY_ICON
import com.ismail.creatvt.indigowallet.utility.IconFactory

class CategoryIconsDialogFragment :
    DataBindingBottomSheetDialogFragment<FragmentCategoryIconsDialogBinding>(R.layout.fragment_category_icons_dialog),
    IconClickListener {

    lateinit var onIconSelectedListener: IconSelectedListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val icon = requireArguments().getString(CATEGORY_ICON)?:"icon_category_question"
        binding?.adapter = CategoryIconsAdapter(icon, this)
        binding?.colorAdapter = CategoryColorAdapter()

        val currentPosition = IconFactory.categoryIcons.indexOfFirst {
            it.name == icon
        }
        val layoutManager: RecyclerView.LayoutManager = binding?.categoryIconList?.layoutManager?:return
        layoutManager.scrollToPosition(currentPosition)
    }

    override fun onIconClick(view: View, icon: IconFactory.Icon) {
        onIconSelectedListener.onIconSelected(icon)
        dismiss()
    }

    interface IconSelectedListener{
        fun onIconSelected(icon: IconFactory.Icon)
    }
}