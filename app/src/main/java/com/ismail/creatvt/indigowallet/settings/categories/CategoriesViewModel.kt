package com.ismail.creatvt.indigowallet.settings.categories

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.settings.categories.edit.AddEditCategoryDialogFragment

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    var type: Int = TYPE_INCOME

    var viewContract: ViewContract? = null

    fun onAddCategoryClick(view: View) {
        viewContract?.showDialogFragment(
            AddEditCategoryDialogFragment(0, false, type), "AddCategory"
        )
    }

}