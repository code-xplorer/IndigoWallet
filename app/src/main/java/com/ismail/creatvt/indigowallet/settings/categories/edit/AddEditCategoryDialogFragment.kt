package com.ismail.creatvt.indigowallet.settings.categories.edit

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingBottomSheetDialogFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentAddEditCategoryDialogBinding
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction


class AddEditCategoryDialogFragment(
    val categoryId: Int,
    val lastCategory:Boolean,
    @MoneyTransaction.TransactionType val type: Int
) :
    DataBindingBottomSheetDialogFragment<FragmentAddEditCategoryDialogBinding>(R.layout.fragment_add_edit_category_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(
            this, CategoryViewModelFactory(
                requireContext().applicationContext as Application,
                categoryId,
                lastCategory,
                type,
                requireActivity()
            )
        ).get(AddEditCategoryViewModel::class.java)

        viewModel.viewContract = this
        binding?.viewModel = viewModel
        binding?.adapter = CategoryColorAdapter()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

}