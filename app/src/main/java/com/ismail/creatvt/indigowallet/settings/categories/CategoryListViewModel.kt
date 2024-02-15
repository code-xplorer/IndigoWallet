package com.ismail.creatvt.indigowallet.settings.categories

import android.app.Application
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.snackbar.Snackbar
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.settings.categories.edit.AddEditCategoryDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryListViewModel(application: Application) : AndroidViewModel(application),
    CategoryClickListener {

    val db = IndigoWalletDatabase.getInstance(application)
    val incomeCategories = db.categoryDao().getIncomeCategories()
    val expenseCategories = db.categoryDao().getExpenseCategories()

    var viewContract: ViewContract? = null

    override fun onCategoryClick(view: View, category: Category) {
        val lastCategory = if(category.type == TYPE_INCOME){
            incomeCategories.value?.size?:0 <= 1
        } else{
            expenseCategories.value?.size?:0 <= 1
        }
        viewContract?.showDialogFragment(
            AddEditCategoryDialogFragment(category.id, lastCategory, category.type),
            "EditCategory"
        )
    }

}