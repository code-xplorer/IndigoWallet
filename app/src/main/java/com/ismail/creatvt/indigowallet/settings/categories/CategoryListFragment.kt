package com.ismail.creatvt.indigowallet.settings.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentCategoryListBinding
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.settings.categories.edit.AddEditCategoryDialogFragment

class CategoryListFragment(@MoneyTransaction.TransactionType val categoryType: Int) :
    DataBindingFragment<FragmentCategoryListBinding>(R.layout.fragment_category_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(
            CategoryListViewModel::class.java)
        viewModel.viewContract = this

        binding?.adapter = CategoryListAdapter(
            if (categoryType == TYPE_INCOME) viewModel.incomeCategories
            else viewModel.expenseCategories,
            viewModel,
            this
        )
    }

}